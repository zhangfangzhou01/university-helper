package com.yhm.universityhelper.authentication;

import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.entity.vo.ResultEnum;
import com.yhm.universityhelper.util.JsonUtils;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        ResponseResult<Object> responseResult;
        if (e instanceof UsernameNotFoundException) {
            responseResult = ResponseResult.fail(ResultEnum.USER_ACCOUNT_NOT_EXIST, e.getMessage());
        } else if (e instanceof DisabledException) {
            responseResult = ResponseResult.fail(ResultEnum.USER_ACCOUNT_DISABLED, e.getMessage());
        } else if (e instanceof LockedException) {
            responseResult = ResponseResult.fail(ResultEnum.USER_ACCOUNT_LOCKED, e.getMessage());
        } else {
            responseResult = ResponseResult.fail(ResultEnum.PASSWORD_ERROR, e.getMessage());
        }
        JsonUtils.writeJson(response, responseResult);
    }
}
