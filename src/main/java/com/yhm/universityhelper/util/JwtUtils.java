package com.yhm.universityhelper.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.useragent.Platform;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
    @Autowired
    private RedisUtils redisUtils;

    // 生成JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setHeaderParam("token", "JWT")
                .setSubject(IdUtil.fastSimpleUUID())
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // 解析JWT
    public Claims getClaimsByToken(String headToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(headToken)
                .getBody();

        String redisToken = (String)redisUtils.get("token:" + getUsernameByToken(claims));
        if (ObjectUtils.isEmpty(redisToken) || !redisToken.equals(headToken)) {
            return null;
        }

        // 判断token是否已经将要过期，如果是，则重新生成token，否则，不做处理
        synchronized (this) {
            if (isTokenNeedRefresh("token:" + getUsernameByToken(claims))) {
                String newToken = generateToken(getUsernameByToken(claims));
                redisUtils.set("newToken:" + getUsernameByToken(claims), newToken, expire);
            }
        }

        return claims;
    }

    public String getUsernameByToken(Claims claims) {
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
    
    public void saveToken(String username, String token) {
        redisUtils.set("token:" + username, token, expire);
    }
    
    public void persistToken(String username, Platform platform) {
        if (platform.isMobile()) {
            redisUtils.persist("token:" + username);
        }
    }
}
