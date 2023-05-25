package com.yhm.universityhelper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Shinki
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(SecurityConfig.RESOURCE_WHITELIST)
                .excludePathPatterns(SecurityConfig.PAGE_WHITELIST)
                .excludePathPatterns(SecurityConfig.LOGIN_WHITELIST);
    }
}
