package com.yhm.universityhelper.util;

import cn.hutool.core.util.IdUtil;
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

import java.util.Date;

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
        String identifier = (String)redisUtils.get("identifier:" + username);
        if (ObjectUtils.isEmpty(identifier)) {
            identifier = IdUtil.fastSimpleUUID();
            redisUtils.set("identifier:" + username, identifier);
        }

        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + 1000 * expire);

        return Jwts.builder()
                .setHeaderParam("token", "JWT")
                .setSubject(username)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .claim("identifier", identifier)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // 解析JWT
    public Claims getClaimsByToken(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(jwt)
                .getBody();
        
        String identifier = (String)redisUtils.get("identifier:" + claims.getSubject());
        if (ObjectUtils.isEmpty(identifier) || !identifier.equals(claims.get("identifier"))) {
            return null;
        }

        // 验证token是否过期
        if (isTokenExpired(claims)) {
            return null;
        }

        // 判断token是否已经将要过期，如果是，则重新生成token，否则，不做处理
        synchronized (this) {
            Date expireDate = claims.getExpiration();
            Date nowDate = new Date();
            if (expireDate.getTime() - nowDate.getTime() < 1000 * refresh) {
                String newToken = generateToken(claims.getSubject());
                redisUtils.set("token:" + claims.getSubject(), newToken);
            }
        }

        return claims;
    }

    public static boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public void expire(String username) {
        String identifier = IdUtil.fastSimpleUUID();
        redisUtils.set("identifier:" + username, identifier);
    }
}
