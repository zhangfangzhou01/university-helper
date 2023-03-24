package com.yhm.universityhelper.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.dao.UserRoleMapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.po.UserRole;
import com.yhm.universityhelper.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean register(String username, String password) {
        User existUser = userMapper.selectByUsername(username);
        if (ObjectUtils.isNotEmpty(existUser)) {
            return false;
        }
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setCreateTime(LocalDateTime.now());
        boolean result = userMapper.insert(user) > 0;

        if (!result) {
            return false;
        }

        UserRole userRole = new UserRole(user.getUserId(), UserRole.ROLE_USER);
        userRoleMapper.insert(userRole);

        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        if (!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        String encodePassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodePassword);
        return userMapper.update(user, null) > 0;
    }
}
