package com.example.task.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {

    private String accessToken;

    private String refreshToken;
    private UserInfoDetails user;

    @Data
    @Builder
    public static class UserInfoDetails{
        private Long id;
        private String email;
        private String username;
    }
}
