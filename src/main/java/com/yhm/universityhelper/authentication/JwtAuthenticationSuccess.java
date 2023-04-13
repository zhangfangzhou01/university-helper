package com.yhm.universityhelper.authentication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.IpUtils;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.JwtUtils;
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

@Component
public class JwtAuthenticationSuccess implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;

    @Lazy
    @Autowired
    private UserService userService;

    @Autowired
    @Value("${authentication.jwt.expire}")
    private long expire;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));

        if (!rememberMe) {
            jwtUtils.setExpiration(0L);
        } else {
            jwtUtils.setExpiration(expire);
        }
        // 除了new UsernamePasswordAuthenticationToken() 外，第二种生成 Token的方式
        String token = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(), token);

        ResponseResult<HashMap<String, Object>> responseResult = ResponseResult.ok(new HashMap<String, Object>() {{
            put("token", token);
        }}, "登录成功");

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, authentication.getName()));
        String region = IpUtils.getRegion(request);
        user.setRegion(region);
        userService.updateById(user);
        responseResult.getData().put("region", user.getRegion());

        JsonUtils.writeJson(response, responseResult);
    }
}
