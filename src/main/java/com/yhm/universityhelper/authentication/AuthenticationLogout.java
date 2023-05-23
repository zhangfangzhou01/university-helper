package com.yhm.universityhelper.authentication;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.entity.vo.ResponseResult;
import com.yhm.universityhelper.util.JsonUtils;
import com.yhm.universityhelper.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationLogout implements LogoutSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (ObjectUtils.isNotEmpty(authentication)) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
            response.setHeader(jwtUtils.getHeader(), "");
            JsonUtils.writeJson(response, ResponseResult.ok("退出成功"));
            jwtUtils.expireToken(authentication.getName());
        } else {
            JsonUtils.writeJson(response, ResponseResult.fail("退出失败"));
        }
    }
}
