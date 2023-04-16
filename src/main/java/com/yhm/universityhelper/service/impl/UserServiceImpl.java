package com.yhm.universityhelper.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UserRoleMapper;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.entity.po.*;
import com.yhm.universityhelper.service.TaskService;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.IpUtils;
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

    @Override
    public boolean register(String username, String password) {
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setCreateTime(LocalDateTime.now());
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
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
        User user = userMapper.selectByUsername(username);
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        boolean result = userMapper.update(user, new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId())) > 0;

        if (!result) {
            throw new RuntimeException("修改密码失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean ban(String username, boolean ban) {
        User user = userMapper.selectByUsername(username);
        user.setBanned(ban);
        boolean result = userMapper.updateById(user) > 0;

        if (!result) {
            throw new RuntimeException("修改用户封禁状态失败，事务回滚");
        }
        return true;
    }

    @Override
    public boolean update(JSONObject json) {
        String username = json.getStr("username");
        User user = userMapper.selectByUsername(username);

        for (String key : json.keySet()) {
            if ("username".equals(key) || "userId".equals(key)) {
                continue;
            }
            ReflectUtils.set(user, key, json.get(key));
        }

        boolean result = userMapper.update(user, new LambdaUpdateWrapper<User>().eq(User::getUsername, username)) > 0;

        if (!result) {
            throw new RuntimeException("修改用户信息失败，事务回滚");
        }
        return true;
    }

    @Override
    public Map<String, Object> select(JSONArray json) {
        List<String> usernames = json.toList(String.class);
        Map<String, Object> users = new HashMap<>();
        for (String username : usernames) {
            User user = userMapper.selectByUsername(username);
            if (ObjectUtils.isEmpty(user)) {
                users.put(username, new HashMap<>());
            } else {
                users.put(username, user);
            }
        }
        return users;
    }

    @Override
    public Map<Long, List<String>> delete(String username) {
        Long userId = userMapper.selectByUsername(username).getUserId();
        boolean result = userMapper.delete(new LambdaUpdateWrapper<User>().eq(User::getUsername, username)) > 0;
        result &= userRoleMapper.delete(new LambdaUpdateWrapper<UserRole>().eq(UserRole::getUserId, userId)) > 0;

        List<Long> taskIds = taskMapper.selectList(new LambdaQueryWrapper<Task>().eq(Task::getUserId, userId))
                .stream()
                .map(Task::getTaskId)
                .collect(Collectors.toList());

        // 删除用户发布的所有任务
        Map<Long, List<String>> taskIdAndUsernames = new HashMap<>();
        for (long taskId : taskIds) {
            taskIdAndUsernames.put(taskId, taskService.delete(taskId));
        }
        // 删除用户接取任务的记录
        List<Long> taskIdsTake = usertaketaskMapper.selectList(new LambdaQueryWrapper<Usertaketask>().eq(Usertaketask::getUserId, userId))
                .stream()
                .map(Usertaketask::getTaskId)
                .collect(Collectors.toList());
        for (long taskId : taskIdsTake) {
            taskService.deleteTaskByTaker(taskId);
        }
        if (!result) {
            throw new RuntimeException("删除用户失败，事务回滚");
        }
        return taskIdAndUsernames;
    }

    @Override
    public boolean setRole(String username, String role) {
        User user = userMapper.selectByUsername(username);
        UserRole userRole = userRoleMapper.selectOne(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));

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
}
