package com.yhm.universityhelper.config;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.util.TokenUtils;
import com.yhm.universityhelper.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shinki
 */
@Configuration
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisUtils redisUtils;

    public String getNewToken(String username) {
        return (String)redisUtils.get("newToken:" + username);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (ObjectUtils.isNull(authentication) || "anonymous".equals(authentication.getPrincipal())) {
            return true;
        }
        
        String username = authentication.getName();
        String token = getNewToken(username);
        if (ObjectUtils.isNotEmpty(token)) {
            response.setHeader(tokenUtils.getHeader(), token);
            redisUtils.delete("newToken:" + username);
        }
        return true;
    }
}
