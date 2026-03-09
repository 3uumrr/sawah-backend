package com.sawah.sawah_backend.security.jwt;

import com.sawah.sawah_backend.security.user.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j // For Logs
@Component
public class JwtUtils {

    @Value("${auth.token.jwt-secret-key}")
    private String jwtSecretKey;

    @Value("${auth.token.expiration-in-mils}")
    private int expirationTime;

    public String generateTokenForUser(Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id",userDetails.getId())
                .claim("roles",roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTime)) // One Hour
                .signWith(key())
                .compact();
    }

    public String getUsernameFromToken(String token){
        return  Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
    }


    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);

            return true;}
        catch (ExpiredJwtException e) {
            log.warn("Token expired: {}", e.getMessage());
            throw new JwtException("expired");
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            throw new JwtException("Invalid token");
        }
    }

    public Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

}
