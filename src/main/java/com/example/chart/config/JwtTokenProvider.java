package com.example.chart.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

    private JwtParser jwtParser;
    private SecretKey key;

    @PostConstruct
    public void init() {
        // Ensure the secret key is long enough for HS256
        if (secretKey.length() < 32) {
            throw new IllegalArgumentException("JWT secret key must be at least 32 characters long");
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.jwtParser = Jwts.parser().verifyWith(key).build();
    }

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .subject(email)
                .claim("roles", roles) // Store roles in JWT
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return jwtParser.parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false; // Invalid or expired token
        }
    }

    public Date extractExpirationTime(String token) {
        return jwtParser.parseSignedClaims(token).getPayload().getExpiration();
    }

    public long getExpirationTime(String token) {
        Date expirationDate = extractExpirationTime(token);
        return expirationDate.getTime() - System.currentTimeMillis();
    }

    // Generate Refresh Token (Longer expiration)
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256) // Use same secret key for simplicity
                .compact();
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        var roles = jwtParser.parseSignedClaims(token).getPayload().get("roles");
        return (List<String>) roles;
    }
}