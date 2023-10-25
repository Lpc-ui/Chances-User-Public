package com.chances.chancesuser.utils;

import com.chances.chancesuser.base.RedisService;
import com.chances.chancesuser.exception.NotLoginException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    @Value("${chances.jwt.secret}")
    private String secret;

    @Value("${chances.jwt.expiration}")
    private Long expiration;
    @Resource
    private RedisService redisService;
    private final String TOKEN_PREFIX = "jwt:";

    @SuppressWarnings("all")
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        // 存储到 Redis 缓存中
        redisService.setCacheObject(TOKEN_PREFIX + username, token, expiration, TimeUnit.SECONDS);

        return token;
    }

    public String getUsernameFromToken(String token) throws NotLoginException {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public void invalidateToken(String username) {
        // 从 Redis 缓存中删除令牌
        redisService.deleteObject(TOKEN_PREFIX + username);
    }

    public boolean isTokenValid(String token, String username) {
        String storedToken = redisService.getCacheObject(TOKEN_PREFIX + username);
        return token.equals(storedToken);
    }

}
