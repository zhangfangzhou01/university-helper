package com.yhm.universityhelper.authentication;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhm.universityhelper.entity.po.User;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.service.UserService;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));

        if (!rememberMe) {
            jwtUtils.setExpiration(0L);
        } else {
            jwtUtils.setExpiration(expire);
        }
        // 除了new UsernamePasswordAuthenticationToken() 外，第二种生成 Token的方式
        Map<String, Object> usernameAndRegion;
        String username;
        String region = null;
        String usernameOrEmail = authentication.getName();
        if (Validator.isEmail(usernameOrEmail)) {
            usernameAndRegion = userService.getMap(new LambdaQueryWrapper<User>().eq(User::getEmail, usernameOrEmail).select(User::getUsername, User::getRegion));
            username = (String) usernameAndRegion.get("username");
            region = (String) usernameAndRegion.get("region");
            if (ObjectUtil.isEmpty(username)) {
                throw new UsernameNotFoundException("用户名或邮箱不存在");
            }
        } else {
            username = usernameOrEmail;
        }

        String token = jwtUtils.generateToken(username);
        response.setHeader(jwtUtils.getHeader(), token);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("region", region);
        ResponseResult<Map<String, Object>> responseResult = ResponseResult.ok(data, "登录成功");

        JsonUtils.writeJson(response, responseResult);
    }
}
