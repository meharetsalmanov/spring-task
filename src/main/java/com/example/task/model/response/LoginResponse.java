package com.example.task.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {

    private String accessToken;

    private String refreshToken;
    private UserDetails userDetails;


    @Data
    @Builder
    public static class UserDetails{
        private String email;
        private Long id;
    }
}
