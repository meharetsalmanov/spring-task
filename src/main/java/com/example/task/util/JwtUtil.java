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

    public Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(securityProperties.getJwt().getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
