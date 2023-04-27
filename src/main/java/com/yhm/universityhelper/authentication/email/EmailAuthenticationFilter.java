package com.yhm.universityhelper.authentication.email;

import com.yhm.universityhelper.authentication.AuthenticationFailure;
import com.yhm.universityhelper.authentication.AuthenticationSuccess;
import com.yhm.universityhelper.dao.UserMapper;
import com.yhm.universityhelper.service.impl.UserDetailsServiceImpl;
import com.yhm.universityhelper.util.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class EmailAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private boolean postOnly = true;

    @Autowired
    private EmailUtils emailUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
    
    @Autowired
    public void setAuthenticationSuccessHandler(AuthenticationSuccess authenticationSuccessHandler) {
        super.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    }
    
    @Autowired
    public void setAuthenticationFailureHandler(AuthenticationFailure authenticationFailureHandler) {
        super.setAuthenticationFailureHandler(authenticationFailureHandler);
    }
    
    public EmailAuthenticationFilter(RequestMatcher requestMatcher, AuthenticationManager authenticationManager) {
        super(requestMatcher, authenticationManager);
    }

    public void setDetails(HttpServletRequest request, EmailAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (postOnly && !"POST".equals(request.getMethod())) {
            throw new IllegalStateException("Authentication method not supported: " + request.getMethod());
        }

        String email = emailUtils.getEmail(request);
        String code = emailUtils.getCode(request);

        Long userId = userMapper.selectUserIdByEmail(email);
        if (userId == null || userId == 0) {
            throw new IllegalStateException("该邮箱未注册");
        }

        List<GrantedAuthority> authorities = userDetailsService.getUserAuthorities(userId);
        EmailAuthenticationToken token = new EmailAuthenticationToken(email, code, authorities);
        this.setDetails(request, token);
        return this.getAuthenticationManager().authenticate(token);
    }
}
