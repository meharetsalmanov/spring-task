package com.example.task.service;

import com.example.task.model.dto.CustomUserDetails;
import com.example.task.service.security.AccessTokenManager;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final AccessTokenManager accessTokenManager;

    public Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest)  {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return Optional.empty();
        }

        final String token = authHeader.substring(7);
        final Claims claims = accessTokenManager.read(token);
        if (claims.getExpiration().before(new Date())) {
            return Optional.empty();
        }
        return Optional.of(getAuthentication(claims));

    }


    private Authentication getAuthentication(Claims claims) {
        return new UsernamePasswordAuthenticationToken(
                CustomUserDetails.builder()
                        .id(claims.get("id", Long.class))
                        .email(claims.get("email", String.class))
                        .username(claims.get("username", String.class))
                        .build(), "", null);
    }

}
