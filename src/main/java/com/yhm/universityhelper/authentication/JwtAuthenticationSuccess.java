package com.yhm.universityhelper.authentication;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

        String token = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(), token);

        ResponseResult<HashMap<String, Object>> responseResult = ResponseResult.ok(new HashMap<String, Object>() {{
            put("token", token);
        }}, "登录成功");

        JsonUtils.writeJson(response, responseResult);
    }
}
