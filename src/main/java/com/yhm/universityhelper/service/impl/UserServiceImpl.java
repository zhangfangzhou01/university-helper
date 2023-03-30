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
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UsertaketaskMapper usertaketaskMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean register(String username, String password) {
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setCreateTime(LocalDateTime.now());
        boolean result = userMapper.insert(user) > 0;

        UserRole userRole = new UserRole(user.getUserId(), Role.USER);
        result &= userRoleMapper.insert(userRole) > 0;

        if (!result) {
            throw new RuntimeException("注册失败，事务回滚");
        }
        return true;
    }

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
            } else users.put(username, user);
        }
        return users;
    }

    @Override
    public boolean delete(String username) {
        Long userId = userMapper.selectByUsername(username).getUserId();
        boolean result = userMapper.delete(new LambdaUpdateWrapper<User>().eq(User::getUsername, username)) > 0;
        result &= userRoleMapper.delete(new LambdaUpdateWrapper<UserRole>().eq(UserRole::getUserId, userId)) > 0;
        result &= taskMapper.delete(new LambdaUpdateWrapper<Task>().eq(Task::getUserId, userId)) > 0;

        Usertaketask usertaketask = usertaketaskMapper.selectById(userId);
        if (ObjectUtils.isNotEmpty(usertaketask)) {
            usertaketask.setUserId(0L);
            result &= usertaketaskMapper.updateById(usertaketask) > 0;
        }

        if (!result) {
            throw new RuntimeException("删除用户失败，事务回滚");
        }
        return true;
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
