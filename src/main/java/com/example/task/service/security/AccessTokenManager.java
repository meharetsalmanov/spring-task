package com.example.task.service.security;

import com.example.task.entity.User;
import com.example.task.model.security.SecurityProperties;
import com.example.task.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class AccessTokenManager {

    private final SecurityProperties securityProperties;
    private final JwtUtil jwtUtil;

    public String generate(User user) {

        Claims claims = Jwts.claims();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("id", user.getId());

        Date now = new Date();
        Date exp = new Date(now.getTime() + securityProperties.getJwt().getAccessTokenValidityTime());

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(now)
                .setExpiration(exp)
                .addClaims(claims)
                .signWith( jwtUtil.getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }


    public Claims read(String token) {
        return Jwts.parserBuilder()
                .setSigningKey( jwtUtil.getSignKey() )
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmail(String token) {
        return read(token).get("email", String.class);
    }
}