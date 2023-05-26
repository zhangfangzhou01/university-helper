package com.yhm.universityhelper.authentication.token;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.config.SecurityConfig;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.service.impl.UserDetailsServiceImpl;
import com.yhm.universityhelper.util.IpUtils;
import com.yhm.universityhelper.util.TokenUtils;
import com.yhm.universityhelper.util.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtils redisUtils;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    private static void checkIfAccessingPublicApiOrResource(String throwMsg, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = request.getServletPath();
        if (SecurityConfig.isPublicApi(uri) || SecurityConfig.isPublicResource(uri)) {
            chain.doFilter(request, response);
            return;
        }
        throw new JwtException(throwMsg);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(tokenUtils.getHeader());
        if (StrUtil.isBlankOrUndefined(jwt)) {
            checkIfAccessingPublicApiOrResource("token为空", request, response, chain);
            return;
        }

        Claims claims = tokenUtils.getClaimsByToken(jwt);
        if ("token解析失败".equals(claims.getSubject())) {
            checkIfAccessingPublicApiOrResource("token解析失败", request, response, chain);
            return;
        }
        
        if ("token已过期".equals(claims.getSubject())) {
            checkIfAccessingPublicApiOrResource("token已过期", request, response, chain);
            return;
        }

        // 获取用户的权限等信息
        String username = tokenUtils.getUsernameByClaims(claims);

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (ObjectUtils.isEmpty(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, userDetailsService.getUserAuthorities(user.getUserId()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Thread.startVirtualThread(() -> {
                    String region = IpUtils.getRegion(request);
                    userService.update(null, new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId()).set(User::getRegion, region));
                    redisUtils.set("user:region:" + user.getUsername(), region, 10);
                }
        );

        chain.doFilter(request, response);
    }
}
