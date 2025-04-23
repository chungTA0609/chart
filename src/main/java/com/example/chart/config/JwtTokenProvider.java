package com.example.chart.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    private static final String SECRET_KEY = "SuperSecretKeyForJWTsMustBeLongEnoughToBeSecure_123@";
    private static final String REFRESH_SECRET_KEY = "SuperRefreshSecretKeyForJWTsMustBeLongEnoughToBeSecure_123@";

    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds
    private final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24 * 7; // 7 days

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final JwtParser jwtParser;

    public JwtTokenProvider() {
        this.jwtParser = Jwts.parser().verifyWith((SecretKey) key).build();
    }

    public String generateToken(String email, List<String> roles) {
        return Jwts.builder()
                .subject(email)
                .claim("roles", roles) // Store roles in JWT
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
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

    // ✅ Generate Refresh Token (Longer expiration)
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY) // Different secret key
                .compact();
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build()
                .parseSignedClaims(token);
    }

    public List<String> extractRoles(String token) {
        var roles = jwtParser.parseSignedClaims(token).getPayload().get("roles");
        return (List<String>) roles;
    }
}