package com.yhm.universityhelper.config;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.util.JwtUtils;
import com.yhm.universityhelper.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
    private JwtUtils jwtUtils;
    
    @Autowired
    private RedisUtils redisUtils;

    public String getToken() {
        return (String) redisUtils.get("token:" + SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (ObjectUtils.isNotEmpty(getToken())) {
            response.setHeader(jwtUtils.getHeader(), getToken());
            redisUtils.delete("token:" + SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return true;
    }
}
