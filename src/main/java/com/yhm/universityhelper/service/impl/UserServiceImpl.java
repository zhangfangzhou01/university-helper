package com.yhm.universityhelper.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.ReflectUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean update(@NotNull JSONObject json) {
        String username = json.get("username", String.class);
        if (ObjectUtils.isEmpty(username)) {
            return false;
        }

        User user = userMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            return false;
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
    public @NotNull Map<String, Object> select(@NotNull JSONObject json) {
//        List<String> usernames = JsonUtils.jsonToList(json, String.class);
        List<String> usernames = json.get("usernames", List.class);
        Map<String, Object> users = new HashMap<>();
        for (String username : usernames) {
            User user = userMapper.selectByUsername(username);
            if (ObjectUtils.isEmpty(user)) {
                users.put(username, new HashMap<>());
            } else users.put(username, user);
        }
        return users;
    }
}
