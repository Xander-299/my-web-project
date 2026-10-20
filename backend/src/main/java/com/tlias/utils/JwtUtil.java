package com.tlias.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    // 密钥（生产环境建议放配置文件）
    private static final String SECRET = "myJwtSecretKeyMyJwtSecretKey123456";

    // 过期时间（1小时）
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * 生成Token
     */
    public static String generateToken(String subject, Map<String, Object> claims) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析Token
     */
    public static Claims parseToken(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取id
     */
    public static String getSubject(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 判断是否过期
     */
    public static boolean isExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }

}
