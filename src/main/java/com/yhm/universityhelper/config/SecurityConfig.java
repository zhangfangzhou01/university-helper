package com.yhm.universityhelper.config;

import com.yhm.universityhelper.authentication.*;
import com.yhm.universityhelper.authentication.email.EmailAuthenticationFilter;
import com.yhm.universityhelper.authentication.email.EmailAuthenticationProvider;
import com.yhm.universityhelper.authentication.token.TokenAuthenticationFilter;
import com.yhm.universityhelper.authentication.password.PasswordAuthenticationFilter;
import com.yhm.universityhelper.authentication.password.PasswordAuthenticationProvider;
import com.yhm.universityhelper.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {"/", "/index", "/homepage", "/home", "/login", "/logout", "/register", "/email/login", "/sendEmailCode"};
    private static final String[] WEB_WHITELIST = {"/static/**", "/templates/**", "/css/**", "/js/**", "/images/**", "/fonts/**", "/favicon.ico", "/error", "/swagger-ui.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs/**", "/doc.html", "/druid/**"};
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthenticationSuccess authenticationSuccess;
    @Autowired
    private AuthenticationFailure authenticationFailure;
    @Autowired
    private AuthenticationLogout authenticationLogout;
    @Lazy
    @Autowired
    private PasswordAuthenticationProvider passwordAuthenticationProvider;
    @Lazy
    @Autowired
    private EmailAuthenticationProvider emailAuthenticationProvider;
    @Autowired
    private DataSource dataSource;

    // 用户信息
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    // 加密方式
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
        repository.setDataSource(dataSource);
//        repository.setCreateTableOnStartup(true);
        return repository;
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        return new TokenAuthenticationFilter(authenticationManager());
    }

    @Bean
    public EmailAuthenticationFilter emailAuthenticationFilter() throws Exception {
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/email/login", "POST");
        final EmailAuthenticationFilter emailAuthenticationFilter = new EmailAuthenticationFilter(requestMatcher, authenticationManager());
        emailAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccess);
        emailAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailure);
        return emailAuthenticationFilter;
    }

    @Bean
    public PasswordAuthenticationFilter passwordAuthenticationFilter() throws Exception {
        RequestMatcher requestMatcher = new AntPathRequestMatcher("/login", "POST");
        final PasswordAuthenticationFilter passwordAuthenticationFilter = new PasswordAuthenticationFilter(requestMatcher, authenticationManager());
        passwordAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccess);
        passwordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailure);
        return passwordAuthenticationFilter;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // 认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(passwordAuthenticationProvider);
        auth.authenticationProvider(emailAuthenticationProvider);
    }

    // 授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()

                .formLogin().disable()

                // 角色控制， ADMIN 和 USER可以访问 /user/**
                // 仅ADMIN可以访问 /admin/**
                // 两个白名单的URL全部放行
                .authorizeRequests()
                .antMatchers("/user/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .antMatchers(AUTH_WHITELIST)
                .permitAll()
                .antMatchers(WEB_WHITELIST)
                .permitAll()
                .anyRequest()
                .authenticated() // 剩余所有请求者需要身份认证
                

                .and().rememberMe().rememberMeParameter("rememberMe")  // 与前端绑定的标签名
                .tokenValiditySeconds(60 * 60 * 24 * 7) // 7天
                .userDetailsService(userDetailsService())
                .tokenRepository(persistentTokenRepository())

                .and().logout()   //开启注销
                .permitAll()    //允许所有人访问
                .logoutSuccessHandler(authenticationLogout) //注销逻辑处理
                .clearAuthentication(true)  //清除认证信息
                .invalidateHttpSession(true)   //清除session
                .deleteCookies("JSESSIONID")    //删除cookie
                .clearAuthentication(true) //清除认证信息

                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)    //权限不足的时候的逻辑处理
                .authenticationEntryPoint(authenticationEntryPoint)  //未登录时的逻辑处理

                .and()
                .sessionManagement()
                .maximumSessions(3);  // 单用户最大会话数

        http
                .addFilterBefore(tokenAuthenticationFilter(), TokenAuthenticationFilter.class)
                .addFilterAfter(passwordAuthenticationFilter(), TokenAuthenticationFilter.class).authenticationProvider(passwordAuthenticationProvider)
                .addFilterAfter(emailAuthenticationFilter(), TokenAuthenticationFilter.class).authenticationProvider(emailAuthenticationProvider);
    }
}
