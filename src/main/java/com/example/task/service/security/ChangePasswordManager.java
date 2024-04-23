package com.example.task.service.security;

import com.example.task.entity.User;
import com.example.task.exception.BaseException;
import com.example.task.model.security.SecurityProperties;
import com.example.task.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.task.model.enums.response.ErrorResponseMessages.INVALID_TOKEN;
import static com.example.task.model.enums.response.ErrorResponseMessages.TOKEN_EXPIRED;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChangePasswordManager {

    private final SecurityProperties securityProperties;
    private final JwtUtil jwtUtil;

    public String getToken(User user) {

        Claims claims = Jwts.claims();
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());

        Date now = new Date();
        Date exp = new Date(now.getTime() + securityProperties.getJwt().getChangePasswordValidityTime());

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(exp)
                .addClaims(claims)
                .signWith( jwtUtil.getSignKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expirationDate = claims.getExpiration();
            if(!expirationDate.before(new Date())){
                return claims.get("email",String.class);
            }else {
                throw BaseException.of(TOKEN_EXPIRED);
            }
        } catch (Exception e) {
            throw BaseException.of(INVALID_TOKEN);
        }
    }

    private boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date expirationDate = claims.getExpiration();
            return !expirationDate.before(new Date());
        } catch (Exception e) {
            return false;
        }
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