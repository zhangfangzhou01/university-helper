package com.yhm.universityhelper.authentication;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.entity.vo.ResultEnum;
import com.yhm.universityhelper.util.JsonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonUtils.writeJson(response, ResponseResult.fail(ResultEnum.USER_NOT_LOGIN));
    }
}