package com.yhm.universityhelper.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.useragent.Platform;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.yhm.universityhelper.dao.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "authentication.jwt")
public class JwtUtils {
    @Value("${authentication.jwt.expire}")
    private long expire;
    @Value("${authentication.jwt.secret}")
    private String secret;
    @Value("${authentication.jwt.header}")
    private String header;
    @Value("${authentication.jwt.refresh}")
    private long refresh;
    @Value("${authentication.jwt.prefix}")
    private String prefix;
    @Autowired
    private RedisUtils redisUtils;
    private ReentrantLock lock = new ReentrantLock();

    // 生成JWT
    public String generateToken(String username) {
        return appendPrefix(Jwts.builder()
                .setHeaderParam("token", "JWT")
                .setSubject(IdUtil.fastSimpleUUID())
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact());
    }

    // 解析JWT
    public Claims getClaimsByToken(String headToken) {
        String headerTokenWithoutPrefix = removePrefix(headToken);
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(headerTokenWithoutPrefix)
                    .getBody();
        } catch (Exception e) {
            return new DefaultClaims().setSubject("token解析失败");
        }

        String username = getUsernameByClaims(claims);
        String redisToken = (String)redisUtils.get("token:" + username);
        if (ObjectUtils.isEmpty(redisToken) || !redisToken.equals(headToken)) {
            return new DefaultClaims().setSubject("token已过期");
        }

        // 判断token是否已经将要过期，如果是，则重新生成token，否则，不做处理
        if (isTokenNeedRefresh("token:" + username)) {
            updateToken(username);
        }

        return claims;
    }
    
    public void updateToken(String username) {
        lock.lock();
        try {
            String newToken = generateToken(username);
            redisUtils.set("newToken:" + username, newToken, 10);
        } finally {
            lock.unlock();
        }
    }

    public String getUsernameByClaims(Claims claims) {
        return claims.get("username", String.class);
    }

    public boolean isTokenExpired(String key) {
        return !redisUtils.hasKey(key);
    }

    public boolean isTokenNeedRefresh(String key) {
        return redisUtils.hasKey(key) && redisUtils.getExpire(key) < refresh;
    }

    public void expireToken(String username) {
        redisUtils.delete("token:" + username);
    }
    
    public void expireToken(Long userId) {
        final String username = BeanUtils.getBean(UserMapper.class).selectById(userId).getUsername();
        expireToken(username);
    }

    public void saveToken(String username, String token) {
        redisUtils.set("token:" + username, token, expire);
    }

    public void persistToken(String username, Platform platform) {
        if (platform.isMobile()) {
            redisUtils.persist("token:" + username);
        }
    }

    public String removePrefix(String token) {
        if (token.startsWith(prefix)) {
            token = token.substring(prefix.length()).trim();
        } else {
            token = null;
        }
        return token;
    }

    public String appendPrefix(String token) {
        if (!token.startsWith(prefix)) {
            token = prefix + " " + token;
        }
        return token;
    }
}
