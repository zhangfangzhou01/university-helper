package com.yhm.universityhelper.authentication;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.JwtUtils;
import com.yhm.universityhelper.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    @Value("${authentication.jwt.expire}")
    private long expire;
    
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));
        if (!rememberMe) {
            jwtUtils.setExpiration(0L);
        } else {
            jwtUtils.setExpiration(expire);
        }
        
        String username = authentication.getName();
        String token = jwtUtils.generateToken(username);
        response.setHeader(jwtUtils.getHeader(), token);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("region", redisUtils.get("user:region:" + username));
        redisUtils.del("user:region:" + username);
        ResponseResult<Map<String, Object>> responseResult = ResponseResult.ok(data, "登录成功");

        JsonUtils.writeJson(response, responseResult);
    }
}
