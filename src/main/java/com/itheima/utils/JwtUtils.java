package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class JwtUtils {
    // 过期时间 (例如: 1小时)
    private static final long ACCESS_EXPIRE = 3600000;

    //加密算法
    private final static SecureDigestAlgorithm<SecretKey, SecretKey> ALGORITHM = Jwts.SIG.HS256;

    //私钥
    private final static String SECRET = UUID.randomUUID().toString();

    //密钥实例
    public static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    //jwt签发者
    private  final static String JWT_ISS = "tianming";

    //jwt主题
    private final static String SUBJECT = "login";

    /**
     * 生成JWT Token
     * @return the generated JWT token
     */
    public static String generateJwt(Map<String, Object> claims) {
        // 令牌id
        String uuid = UUID.randomUUID().toString();
        Date exprireDate = Date.from(Instant.now().plusSeconds(ACCESS_EXPIRE));

        return Jwts.builder()
                .header()// 设置头部信息header
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                .claims(claims)// 设置自定义负载信息payload
                .id(uuid)// 令牌ID
                .expiration(exprireDate)// 过期日期
                .issuedAt(new Date())// 签发时间
                .subject(SUBJECT)// 主题
                .issuer(JWT_ISS)// 签发者
                .signWith(KEY, ALGORITHM)// 签名
                .compact();
    }

    /**
     * 解析JWT Token
     *
     * @param token the JWT token
     * @return the Claims object containing the token's payload
     */
    public static Jws<Claims> parseJwt(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token);
    }
}
