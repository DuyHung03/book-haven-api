package com.duyhung.bookstoreapi.jwt;

import com.duyhung.bookstoreapi.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtService {

    @Value("${jwt.secret-key.expiration}")
    private Long EXPIRY_DATE;

    @Value("${jwt.secret-key}")
    private String secretKey;
    private SecretKey key;

    private SecretKey getKey() {
        if (key == null) {
            key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        }
        return key;
    }

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRY_DATE))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public Claims extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractClaim(token).getSubject();
    }

    public boolean verifyToken(String token) throws RuntimeException {
        try {
            Claims claims = extractClaim(token);
            Date expirationDate = claims.getExpiration();
            if (expirationDate.before(new Date())) {
                throw new RuntimeException("TOKEN EXPIRE");
            }
            return true;
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature:", e);
        } catch (MalformedJwtException e) {
                throw new RuntimeException("Invalid JWT token" + e);
        } catch (ExpiredJwtException e) {
                throw new RuntimeException("JWT token is expired:", e);
        } catch (UnsupportedJwtException e) {
                throw new RuntimeException("JWT token is unsupported:", e);
        } catch (IllegalArgumentException e) {
                throw new RuntimeException("JWT claims string is empty:", e);
        }
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "accessToken");
        return cookie != null ? cookie.getValue() : null;
    }
}
