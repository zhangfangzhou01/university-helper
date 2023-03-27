package com.yhm.universityhelper.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.TaskMapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UserRoleMapper;
import com.yhm.universityhelper.dao.UsertaketaskMapper;
import com.yhm.universityhelper.entity.po.Task;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.entity.po.Usertaketask;
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
        if (userMapper.exists(new LambdaUpdateWrapper<User>().eq(User::getUsername, username))) {
            throw new RuntimeException("用户名已存在");
        }

        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setCreateTime(LocalDateTime.now());
        boolean result = userMapper.insert(user) > 0;

        if (!result) {
            throw new RuntimeException("注册失败");
        }

        UserRole userRole = new UserRole(user.getUserId(), UserRole.ROLE_USER);
        userRoleMapper.insert(userRole);

        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("用户不存在");
        } else if (user.getBanned()) {
            throw new RuntimeException("用户已被封禁");
        }

        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        return userMapper.update(user, new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId())) > 0;
    }

    @Override
    public boolean ban(String username, boolean ban) {
        User user = userMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }

        user.setBanned(ban);
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean update(JSONObject json) {
        String username = json.get("username", String.class);
        if (ObjectUtils.isEmpty(username)) {
            return false;
        }

        User user = userMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            throw new RuntimeException("用户不存在");
        } else if (user.getBanned()) {
            throw new RuntimeException("用户已被封禁");
        }

        for (String key : json.keySet()) {
            if ("username".equals(key) || "userId".equals(key)) {
                continue;
            }
            ReflectUtils.set(user, key, json.get(key));
        }

        return userMapper.update(user, new LambdaUpdateWrapper<User>().eq(User::getUsername, username)) > 0;
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
        userRoleMapper.delete(new LambdaUpdateWrapper<UserRole>().eq(UserRole::getUserId, userId));
        taskMapper.delete(new LambdaUpdateWrapper<Task>().eq(Task::getUserId, userId));

        Usertaketask usertaketask = usertaketaskMapper.selectById(userId);
        if (ObjectUtils.isNotEmpty(usertaketask)) {
            usertaketask.setUserId(0L);
            usertaketaskMapper.updateById(usertaketask);
        }

        return result;
    }
}
