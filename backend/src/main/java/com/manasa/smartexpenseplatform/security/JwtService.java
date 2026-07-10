package com.manasa.smartexpenseplatform.security;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Date;
@Service
public class JwtService {
    private static final String SECRET_KEY = "mySecretKeyForSmartExpensePlatformJwtAuthentication123456";
    public String generateToken(String email) {
        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSigningKey())
            .compact();
        }
    private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(
            SECRET_KEY.getBytes(StandardCharsets.UTF_8)
    );
}
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    } 
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }  
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public boolean isTokenValid(String token, String email) {
        String username = extractUsername(token);

        return username.equals(email) && !isTokenExpired(token);
    }
}