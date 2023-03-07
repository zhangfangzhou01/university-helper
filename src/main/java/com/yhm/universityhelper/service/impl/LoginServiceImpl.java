package com.yhm.universityhelper.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean register(String username, String password) {
        User existUser = userMapper.selectByUsername(username);
        if (ObjectUtils.isNotEmpty(existUser)) {
            return false;
        }
        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        return userMapper.insert(user) > 0;
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
