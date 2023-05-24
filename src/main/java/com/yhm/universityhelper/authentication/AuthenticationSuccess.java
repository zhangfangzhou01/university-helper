package com.yhm.universityhelper.authentication;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.util.*;
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
        if (!redisUtils.hasKey("user:region:" + authentication.getName())) {
            String region = IpUtils.getRegion(request);
            redisUtils.set("user:region:" + authentication.getName(), region, 10);
            Thread.startVirtualThread(() -> userMapper.update(null, new LambdaUpdateWrapper<User>().eq(User::getUsername, authentication.getName()).set(User::getRegion, region)));
        }

        String username = authentication.getName();
        String token = jwtUtils.generateToken(username);
        jwtUtils.saveToken(username, token);
        jwtUtils.persistToken(username, DeviceUtils.getPlatform(request));
        response.setHeader(jwtUtils.getHeader(), token);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("region", redisUtils.get("user:region:" + username));
        redisUtils.delete("user:region:" + username);
        ResponseResult<Map<String, Object>> responseResult = ResponseResult.ok(data, "登录成功");

        JsonUtils.writeJson(response, responseResult);
    }
}
