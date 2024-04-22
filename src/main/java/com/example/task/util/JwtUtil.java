package com.example.task.util;

import com.example.task.entity.User;
import com.example.task.model.security.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final SecurityProperties securityProperties;


    public String accessToken(User user) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwt().getAccessTokenValidityTime()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .setClaims(createClaims(user))
                .compact();
    }

    public String refreshToken(User user) {
        Map<String, Object> claims = createClaims(user);
        claims.put("type","REFRESH_TOKEN");
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + securityProperties.getJwt().getRefreshTokenValidityTime()))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .setClaims(claims)
                .compact();
    }

    private Map<String, Object> createClaims(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("email",user.getEmail());
        claims.put("id",user.getId());
        return claims;
    }



    public Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(securityProperties.getJwt().getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims parseToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
