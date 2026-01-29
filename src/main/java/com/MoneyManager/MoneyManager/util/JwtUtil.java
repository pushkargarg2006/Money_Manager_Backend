package com.MoneyManager.MoneyManager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // 🔐 Secret key (minimum 32 chars REQUIRED for HS256)
    private static final String SECRET_KEY =
            "myCollegeSecretKeyForJwtGeneration12345";

    // Token validity: 1 hour
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    // 🔑 Key object (NEW WAY)
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // ================== GENERATE TOKEN ==================
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256) 
                .compact();
    }

    // ================== EXTRACT USERNAME ==================
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // ================== VALIDATE TOKEN ==================
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ================== TOKEN EXPIRED CHECK ==================
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // ================== GET CLAIMS ==================
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()                    .setSigningKey(key)                    .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
