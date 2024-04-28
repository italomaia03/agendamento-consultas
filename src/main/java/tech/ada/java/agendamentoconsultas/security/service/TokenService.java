package tech.ada.java.agendamentoconsultas.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generatedToken(UserDetails userDetails) {
        LocalDateTime now = LocalDateTime.now();
        return Jwts.builder()
                .claims(new HashMap<>())
                .subject(userDetails.getUsername())
                .expiration(convertFromLocalDateTime(now.plusHours(1L)))
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey(){
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

    public String extractUsername(String token) {
        return this.extractClaims(token, Claims::getSubject);
    }

    private Date convertFromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final var claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final var username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
