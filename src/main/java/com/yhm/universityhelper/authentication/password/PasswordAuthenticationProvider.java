package com.yhm.universityhelper.authentication.password;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.entity.dto.LoginUser;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.service.impl.UserDetailsServiceImpl;
import com.yhm.universityhelper.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class PasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    @Value("${account.locked.max-failed-attempts}")
    private int maxFailedAttempts;

    @Autowired
    @Value("${account.locked.lock-time}")
    private int lockTime;

    @Autowired
    @Value("${account.locked.remote-login-lock-time}")
    private int remoteLoginLockTime;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        
        User user;
        if (Validator.isEmail(authentication.getName())) {
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, authentication.getName()));
            if (ObjectUtil.isEmpty(user)) {
                throw new UsernameNotFoundException("用户名或邮箱不存在");
            }
        } else {
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, authentication.getName()));
        }
        
        String rawPassword = authentication.getCredentials().toString();
        LoginUser loginUser = userDetailsService.loadUserByUser(user);

        if (ObjectUtils.isEmpty(loginUser)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        if (!loginUser.isEnabled()) {
            throw new DisabledException("用户已被封禁，请联系管理员");
        }

        if (user.getPasswordErrorCount() >= maxFailedAttempts) {
            if (user.getUnlockTime().isAfter(LocalDateTime.now())) {
                long time = user.getUnlockTime().toEpochSecond(ZoneOffset.of("+8")) - LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
                throw new LockedException("密码错误次数过多，用户已被锁定，请" + time + "秒后重试");
            } else {
                user.setPasswordErrorCount(0);
                userService.update(null, new LambdaUpdateWrapper<User>().eq(User::getUsername, user.getUsername()).set(User::getPasswordErrorCount, 0));
            }
        }

        boolean checkPassword = bCryptPasswordEncoder.matches(rawPassword, loginUser.getPassword());
        if (!checkPassword) {
            user.setPasswordErrorCount(user.getPasswordErrorCount() + 1);
            if (user.getPasswordErrorCount() >= maxFailedAttempts) {
                if (loginUser.getRegion().equals(user.getRegion())) {
                    user.setUnlockTime(LocalDateTime.now().plusSeconds(lockTime));
                } else {
                    user.setUnlockTime(LocalDateTime.now().plusSeconds(remoteLoginLockTime));
                    userService.update(null, new LambdaUpdateWrapper<User>().eq(User::getUsername, user.getUsername()).set(User::getUnlockTime, LocalDateTime.now().plusSeconds(remoteLoginLockTime)));
                    throw new LockedException("疑似账号被盗，已被锁定" + remoteLoginLockTime / 60 + "分钟");
                }

            }
            userService.updateById(user);
            throw new BadCredentialsException("密码错误，请重新输入");
        }

        user.setPasswordErrorCount(0);
        userService.update(null, new LambdaUpdateWrapper<User>().eq(User::getUsername, user.getUsername()).set(User::getPasswordErrorCount, 0));
        return new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword, loginUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
