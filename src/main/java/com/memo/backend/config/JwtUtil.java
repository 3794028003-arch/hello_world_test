package com.memo.backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long accessExpirationMillis;
    private final long refreshExpirationMillis;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-expiration-ms}") long accessExpirationMillis,
                   @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMillis = accessExpirationMillis;
        this.refreshExpirationMillis = refreshExpirationMillis;
    }

    public String generateAccessToken(String username) {
        return buildToken(username, "access", null, accessExpirationMillis);
    }

    public String generateRefreshToken(String username, String tokenId) {
        return buildToken(username, "refresh", tokenId, refreshExpirationMillis);
    }

    private String buildToken(String username, String type, String tokenId, long expirationMillis) {
        Date now = new Date();
        var builder = Jwts.builder().subject(username).claim("type", type).issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMillis)).signWith(secretKey).compact();
        if (tokenId != null) {
            return Jwts.builder().subject(username).claim("type", type).id(tokenId).issuedAt(now)
                    .expiration(new Date(now.getTime() + expirationMillis)).signWith(secretKey).compact();
        }
        return builder;
    }

    public String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public String getTokenType(String token) { return parseToken(token).get("type", String.class); }
    public String getTokenId(String token) { return parseToken(token).getId(); }
    public long getRefreshExpirationMillis() { return refreshExpirationMillis; }
}
