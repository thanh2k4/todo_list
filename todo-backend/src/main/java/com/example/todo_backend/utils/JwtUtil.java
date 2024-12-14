package com.example.todo_backend.utils;

import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(System.getProperty("JWT_SECRET").getBytes());

    public String generateToken() {
        String userId = UUID.randomUUID().toString();
        return Jwts.builder()
                .claim("sub", userId)
                .claim("iat", new Date())
                .signWith(secretKey)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
