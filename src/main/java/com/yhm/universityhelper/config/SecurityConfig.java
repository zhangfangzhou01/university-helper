package com.yhm.universityhelper.config;

import com.yhm.universityhelper.authentication.*;
import com.yhm.universityhelper.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/index",
            "/homepage",
            "/home",
            "/login",
            "/logout",
            "/register"
    };

    private static final String[] WEB_WHITELIST = {
            "/static/**",
            "/templates/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/fonts/**",
            "/favicon.ico",
            "/error",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/api-docs/**",
            "/doc.html",
            "/druid/**"
    };

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationSuccess authenticationSuccess;
    @Autowired
    private JwtAuthenticationFailure authenticationFailure;
    @Autowired
    private JwtAuthenticationLogout authenticationLogout;
    @Lazy
    @Autowired
    private JwtAuthenticationProvider authenticationProvider;
    @Autowired
    private JwtAccessDeniedHandler accessDeniedHandler;
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
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter(authenticationManager());
    }

    // 认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    // 授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()

                .addFilterBefore(jwtAuthenticationFilter(), JwtAuthenticationFilter.class)

                .authorizeRequests()
                // 角色控制， ADMIN 和 USER可以访问 /user/**
                // 仅ADMIN可以访问 /admin/**
                // 两个白名单的URL全部放行
                .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers(WEB_WHITELIST).permitAll()
                .anyRequest().authenticated() // 剩余所有请求者需要身份认证

                .and()
                .formLogin()  //开启登录
                .permitAll()  //允许所有人访问
                .successHandler(authenticationSuccess) // 登录成功逻辑处理
                .failureHandler(authenticationFailure) // 登录失败逻辑处理

                .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")  // 与前端绑定的标签名
                .tokenValiditySeconds(60 * 60 * 24 * 7) // 7天
                .userDetailsService(userDetailsService())
                .tokenRepository(persistentTokenRepository())

                .and()
                .logout()   //开启注销
                .permitAll()    //允许所有人访问
                .logoutSuccessHandler(authenticationLogout) //注销逻辑处理
                .clearAuthentication(true)  //清除认证信息
                .invalidateHttpSession(true)   //清除session
                .deleteCookies("JSESSIONID")    //删除cookie
                .clearAuthentication(true) //清除认证信息

                .and().exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)    //权限不足的时候的逻辑处理
                .authenticationEntryPoint(authenticationEntryPoint)  //未登录时的逻辑处理

                .and()
                .sessionManagement()
                .maximumSessions(3);  // 单用户最大会话数
    }
}
