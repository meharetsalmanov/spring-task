package com.example.task.service;

import com.example.task.entity.User;
import com.example.task.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public Optional<Authentication> getAuthentication(HttpServletRequest httpServletRequest) throws ServletException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return Optional.empty();
        }

        final String token = authHeader.substring(7);
        final Claims claims = jwtUtil.parseToken(token);
        if (claims.getExpiration().before(new Date())) {
            return Optional.empty();
        }
        return Optional.of(getAuthentication(claims));

    }
    private Authentication getAuthentication(Claims claims) {
        UserDetails user = User.builder()
                .id(Long.valueOf(claims.get("id").toString()))
                .email(claims.get("email").toString())
                .build();
        return new UsernamePasswordAuthenticationToken(user, "", null);
    }

}
