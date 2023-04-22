package com.yhm.universityhelper.authentication.email;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yhm.universityhelper.entity.dto.LoginUser;
import com.yhm.universityhelper.service.impl.UserDetailsServiceImpl;
import com.yhm.universityhelper.util.EmailUtils;
import com.yhm.universityhelper.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class EmailAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
    @Autowired
    private RedisUtils redisUtils;
    
    @Autowired
    private EmailUtils emailUtils;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        log.info("EmailAuthentication authentication request: {}", authentication);
        String codeReceived = (String) authentication.getCredentials();
        String codeStored = (String)redisUtils.get(emailUtils.getEmail(authentication));

        if (StringUtils.isBlank(codeReceived)) {
            throw new BadCredentialsException("验证码不能为空");
        }

        if (StringUtils.isBlank(codeStored)) {
            throw new BadCredentialsException("验证码已过期或不存在，请重新获取");
        }

        boolean result = Objects.equals(codeReceived, codeStored);
        if (result) {
            redisUtils.del(emailUtils.getEmail(authentication));
        }
        
        LoginUser loginUser = userDetailsService.loadUserByEmail((String)authentication.getPrincipal());
        
        if (loginUser == null) {
            throw new InternalAuthenticationServiceException("无法获取到用户信息，用户可能未设定该邮箱或该邮箱未注册");
        }
        
        return new EmailAuthenticationToken(loginUser, loginUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
