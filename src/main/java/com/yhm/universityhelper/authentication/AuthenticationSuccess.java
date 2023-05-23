package com.yhm.universityhelper.authentication;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yhm.universityhelper.config.SecurityConfig;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.util.IpUtils;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.JwtUtils;
import com.yhm.universityhelper.util.RedisUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthenticationSuccess implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String formerToken = request.getHeader(jwtUtils.getHeader());
        if (StringUtils.isEmpty(formerToken) || ObjectUtils.isEmpty(jwtUtils.getClaimsByToken(formerToken))) {
            String uri = request.getServletPath();
            if (ArrayUtil.contains(SecurityConfig.AUTH_WHITELIST, uri) || ArrayUtil.contains(SecurityConfig.WEB_WHITELIST, uri)) {
                String region = IpUtils.getRegion(request);
                redisUtils.set("user:region:" + authentication.getName(), region);
                Thread.startVirtualThread(() -> userMapper.update(null, new LambdaUpdateWrapper<User>().eq(User::getUsername, authentication.getName()).set(User::getRegion, region)));
            }
        }

        String username = authentication.getName();
        String token = jwtUtils.generateToken(username);
        response.setHeader(jwtUtils.getHeader(), token);

        boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));
        if (!rememberMe) {
            jwtUtils.expireToken(username);
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("region", redisUtils.get("user:region:" + username));
        redisUtils.del("user:region:" + username);
        ResponseResult<Map<String, Object>> responseResult = ResponseResult.ok(data, "登录成功");

        JsonUtils.writeJson(response, responseResult);
    }
}
