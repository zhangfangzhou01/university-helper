package com.yhm.universityhelper.authentication;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.entity.vo.ResultEnum;
import com.yhm.universityhelper.util.JsonUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFailure implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, @NotNull HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ResponseResult responseResult;
        if (exception instanceof UsernameNotFoundException) {
            responseResult = ResponseResult.failure(ResultEnum.USER_ACCOUNT_NOT_EXIST);
        } else {
            responseResult = ResponseResult.failure(ResultEnum.PASSWORD_ERROR);
        }
        JsonUtils.writeJson(response, responseResult);
    }
}
