package com.yhm.universityhelper.authentication;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.JwtUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
    JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Authentication authentication) throws IOException, ServletException {
        String token = jwtUtils.generateToken(authentication.getName());
        response.setHeader(jwtUtils.getHeader(), token);

        ResponseResult responseResult = ResponseResult.success(new HashMap<String, Object>() {{
            put("token", token);
        }}, "登录成功");

        JsonUtils.writeJson(response, responseResult);
    }
}
