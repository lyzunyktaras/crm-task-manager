package com.sample.crm.util;

import com.sample.crm.model.security.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtHelper {
    private static final String CLAIM_KEY_PERMISSION = "prm";
    private static final String CLAIM_KEY_USER_PRINCIPAL_ID = "uid";

    @Value("${crm.jwt.secret}")
    private String secret;

    @Value("${crm.jwt.expiration}")
    private Duration expiration;

    private Key key;

    @PostConstruct
    public void postConstruct() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateJwtToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Map<String, Object> permissions = new HashMap<>();

        permissions.put(CLAIM_KEY_PERMISSION,
                userPrincipal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet()));

        Map<String, Object> id = new HashMap<>();

        id.put(CLAIM_KEY_USER_PRINCIPAL_ID, userPrincipal.getId());

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .addClaims(permissions)
                .addClaims(id)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(new Date().toInstant().plus(expiration)))
                .signWith(key)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
