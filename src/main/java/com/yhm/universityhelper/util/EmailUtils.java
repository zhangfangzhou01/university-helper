package com.yhm.universityhelper.util;

import cn.hutool.core.util.RandomUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Data
@Component
@ConfigurationProperties(prefix = "authentication.email")
public class EmailUtils {
    @Value("${authentication.email.request.email-name}")
    private String emailName;
    @Value("${authentication.email.request.code-name}")
    private String codeName;
    @Value("${authentication.email.code.length}")
    private int length;
    @Value("${authentication.email.code.expire}")
    private int expire;
    @Value("${authentication.email.url}")
    private String url;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JavaMailSender mailSender;

    public String generateCode() {
        return RandomUtil.randomString(length);
    }

    public String getEmail(HttpServletRequest request) {
        String email = request.getParameter(emailName);
        if (email == null) {
            email = "";
        }
        return email.trim();
    }
    
    public String getCode(HttpServletRequest request) {
        String code = request.getParameter(codeName);
        if (code == null) {
            code = "";
        }
        return code.trim();
    }
    
    public String getEmail(Authentication authentication) {
        return (String) authentication.getPrincipal();
    }
    
    public String getCode(Authentication authentication) {
        return (String) authentication.getCredentials();
    }

    @SneakyThrows
    public void send(String from, String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
