package com.example.bankcards.security.jwt;

import com.example.bankcards.util.HmacUtils;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    @Value("${app.jwt.secretKey}")
    private String secretKey;

    @Value("${app.jwt.tokenExpiration}")
    private Duration tokenExpiration;

    public String generateJwtToken(String subject) {
        return Jwts.builder()
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .signWith(HmacUtils.fromBase64(secretKey))
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(HmacUtils.fromBase64(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser().verifyWith(HmacUtils.fromBase64(secretKey)).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT token. Message: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Token validation failed. Message: {}", e.getMessage());
        }

        return false;
    }
}
