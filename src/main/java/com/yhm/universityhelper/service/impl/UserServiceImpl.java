package com.yhm.universityhelper.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.*;
import com.yhm.universityhelper.entity.po.*;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.EmailUtils;
import com.yhm.universityhelper.util.IpUtils;
import com.yhm.universityhelper.util.RedisUtils;
import com.yhm.universityhelper.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yhm
 * @since 2023-02-26
 */

@Transactional
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UsertaketaskMapper usertaketaskMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private BlacklistMapper blacklistMapper;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public boolean register(String username, String password) {
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setCreateTime(LocalDateTime.now());
        HttpServletRequest request = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        user.setRegion(IpUtils.getRegion(request));
        boolean result = userMapper.insert(user) > 0;

        UserRole userRole = new UserRole(user.getUserId(), Role.USER);
        result &= userRoleMapper.insert(userRole) > 0;

        if (!result) {
            throw new RuntimeException("注册失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        boolean result = userMapper.update(user, new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId())) > 0;

        if (!result) {
            throw new RuntimeException("修改密码失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean ban(Long userId, boolean ban) {
        User user = userMapper.selectById(userId);
        user.setBanned(ban);
        boolean result = userMapper.updateById(user) > 0;

        if (!result) {
            throw new RuntimeException("修改用户封禁状态失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean update(JSONObject json) {
        Long userId = json.getLong("userId");
        User user = userMapper.selectById(userId);

        for (String key : json.keySet()) {
            if ("username".equals(key) || "userId".equals(key)) {
                continue;
            }
            ReflectUtils.set(user, key, json.get(key));
        }

        boolean result = userMapper.updateById(user) > 0;

        if (!result) {
            throw new RuntimeException("修改用户信息失败，事务回滚");
        }
        return true;
    }

    @Override
    public Map<String, Object> select(JSONArray json) {
        List<Long> userIds = json.toList(Long.class);
        Map<String, Object> users = new HashMap<>();
        for (Long userId : userIds) {
            User user = userMapper.selectById(userId);
            if (ObjectUtils.isEmpty(user)) {
                users.put(String.valueOf(userId), null);
            } else {
                users.put(String.valueOf(userId), user);
            }
        }
        return users;
    }
    
    @Override
    public Map<Long, List<String>> delete(Long userId) {
        boolean result = userMapper.deleteById(userId) > 0;
        result &= userRoleMapper.delete(new LambdaUpdateWrapper<UserRole>().eq(UserRole::getUserId, userId)) > 0;

        List<Long> taskIds = taskMapper.selectList(new LambdaQueryWrapper<Task>().eq(Task::getUserId, userId)).stream().map(Task::getTaskId).collect(Collectors.toList());

        // 删除用户发布的所有任务
        Map<Long, List<String>> taskIdAndUsernames = new HashMap<>();
        for (long taskId : taskIds) {
            taskIdAndUsernames.put(taskId, taskService.delete(taskId));
        }
        // 删除用户接取任务的记录
        List<Long> taskIdsTake = usertaketaskMapper.selectList(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getUserId, userId)).stream().map(Usertaketask::getTaskId).collect(Collectors.toList());
        for (long taskId : taskIdsTake) {
            taskService.deleteTaskByTaker(taskId);
        }

        result &= followMapper.delete(new LambdaUpdateWrapper<Follow>().eq(Follow::getFollowerId, userId)) > 0;
        result &= followMapper.delete(new LambdaUpdateWrapper<Follow>().eq(Follow::getFollowedId, userId)) > 0;
        result &= blacklistMapper.delete(new LambdaUpdateWrapper<Blacklist>().eq(Blacklist::getBlockerId, userId)) > 0;
        result &= blacklistMapper.delete(new LambdaUpdateWrapper<Blacklist>().eq(Blacklist::getBlockedId, userId)) > 0;

        if (!result) {
            throw new RuntimeException("删除用户失败，事务回滚");
        }
        return taskIdAndUsernames;
    }

    @Override
    public boolean setRole(Long userId, String role) {
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        
        if ("admin".equalsIgnoreCase(role)) {
            userRole.setRoleId(Role.ADMIN);
        } else {
            userRole.setRoleId(Role.USER);
        }

        boolean result = userRoleMapper.updateById(userRole) > 0;

        if (!result) {
            throw new RuntimeException("修改用户角色失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean follow(Long followerId, Long followedId) {
        Follow follow = new Follow(followerId, followedId);
        return followMapper.insert(follow) > 0;
    }

    @Override
    public boolean unfollow(Long followerId, Long followedId) {
        return followMapper.delete(new LambdaUpdateWrapper<Follow>().eq(Follow::getFollowerId, followerId).eq(Follow::getFollowedId, followedId)) > 0;
    }

    @Override
    public List<String> selectFollowedList(Long userId) {
        return userMapper.selectFollowedList(userId);
    }

    @Override
    public List<String> selectFollowerList(Long userId) {
        return userMapper.selectFollowerList(userId);
    }

    @Override
    public Long selectFollowedCount(Long userId) {
        return userMapper.selectFollowedCount(userId);
    }

    @Override
    public Long selectFollowerCount(Long userId) {
        return userMapper.selectFollowerCount(userId);
    }

    @Override
    public boolean block(Long blockerId, Long blockedId) {
        Blacklist blacklist = new Blacklist(blockerId, blockedId);
        return blacklistMapper.insert(blacklist) > 0;
    }

    @Override
    public boolean unblock(Long blockerId, Long blockedId) {
        return blacklistMapper.delete(new LambdaUpdateWrapper<Blacklist>().eq(Blacklist::getBlockerId, blockerId).eq(Blacklist::getBlockedId, blockedId)) > 0;
    }

    @Override
    public List<String> selectBlockedList(Long userId) {
        return userMapper.selectBlockedList(userId);
    }

    @Override
    public Long selectBlockedCount(Long userId) {
        return userMapper.selectBlockedCount(userId);
    }

    @Override
    public void sendEmailCode(String to) {
        int expire = emailUtils.getExpire();
        if (redisUtils.hasKey(to)) {
            throw new RuntimeException("请在" + redisUtils.getExpire(to) + "秒后再次尝试");
        }

        String from = emailUtils.getUrl();
        String code = emailUtils.generateCode();
        String subject = "UniversityHelper 邮箱验证码";
        String content = "您的验证码为：" + code + "，请在" + expire + "秒内使用";

        try {
            emailUtils.send(from, to, subject, content);
        } catch (Exception e) {
            throw new RuntimeException("发送邮件失败");
        }

        redisUtils.set(to, code, expire);
    }

    @Override
    public boolean changeEmail(Long userId, String email) {
        return userMapper.update(null, new LambdaUpdateWrapper<User>().eq(User::getUserId, userId).set(User::getEmail, email)) > 0;
    }
}
