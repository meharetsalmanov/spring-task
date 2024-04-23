package com.example.task.model.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class SecurityJwtData {

    @Value("${secret-key}")
    private String secretKey;

    @Value("${access-token-validity-time}")
    private Integer accessTokenValidityTime;

    @Value("${refresh-token-validity-time}")
    private Integer refreshTokenValidityTime;

    @Value("${change-password-validity-time}")
    private Integer changePasswordValidityTime;
}