package com.example.gigwork.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.access-token-validity}")
    private long accessTokenValidity;
    
    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;
    
    private SecretKey key;
    
    @PostConstruct
    public void init() {
        // SecretKey 생성 (최소 256bit 필요)
        byte[] keyBytes = secretKey.getBytes();
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Access Token 생성
     */
    public String createAccessToken(String email, String userType) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidity);
        
        return Jwts.builder()
                .subject(email)
                .claim("userType", userType)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
    
    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(String email) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidity);
        
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
    
    /**
     * Refresh Token 검증
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token);
    }
    
    /**
     * Refresh Token 만료일 계산
     */
    public LocalDateTime getRefreshTokenExpiry() {
        return LocalDateTime.now().plusSeconds(refreshTokenValidity / 1000);
    }
    
    /**
     * 토큰에서 이메일 추출
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    /**
     * 토큰에서 UserType 추출
     */
    public String getUserTypeFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userType", String.class);
    }
    
    /**
     * 토큰 검증
     */
    public boolean validateToken(String token) {
        String masked = token == null ? "null" : (token.length() <= 8 ? token : token.substring(0, 8) + "...");
        try {
            logger.info("validateToken: checking token {}", masked);
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            logger.info("validateToken: token {} is valid", masked);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("validateToken: token {} is invalid: {}", masked, e.getMessage());
            return false;
        }
    }
    
    /**
     * 토큰 만료일자 반환
     */
    public Date getExpirationDate(String token) {
        String masked = token == null ? "null" : (token.length() <= 8 ? token : token.substring(0, 8) + "...");
        try {
            logger.info("getExpirationDate: parsing expiration for token {}", masked);
            Date exp = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
            logger.info("getExpirationDate: token {} expires at {}", masked, exp);
            return exp;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("getExpirationDate: failed to parse expiration for token {}: {}", masked, e.getMessage());
            return null;
        }
    }
    
    /**
     * 토큰 만료 확인
     */
    // public boolean isTokenExpired(String token) {
    //     try {
    //         Date expiration = Jwts.parser()
    //                 .verifyWith(key)
    //                 .build()
    //                 .parseSignedClaims(token)
    //                 .getPayload()
    //                 .getExpiration();
    //         return expiration.before(new Date());
    //     } catch (JwtException | IllegalArgumentException e) {
    //         return true;
    //     }
    // }
}
