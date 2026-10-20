package com.tlias.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JWT 工具类。
 * <p>
 * 签名密钥与过期时间通过 {@code application.yaml} 的 {@code jwt.*} 配置注入，
 * 生产环境建议用环境变量 {@code JWT_SECRET} 覆盖，避免密钥硬编码入库。
 */
@Component
public class JwtUtil {

    /**
     * 签名密钥。生产环境建议通过环境变量 JWT_SECRET 注入。
     */
    @Value("${jwt.secret:myJwtSecretKeyMyJwtSecretKey123456}")
    private String secret;

    /**
     * 过期时间（毫秒），默认 24 小时。
     */
    @Value("${jwt.expiration:86400000}")
    private long expiration;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 生成Token
     */
    public String generateToken(String subject, Map<String, Object> claims) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析Token
     */
    public Claims parseToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取id
     */
    public String getSubject(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 判断是否过期
     */
    public boolean isExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }

}
