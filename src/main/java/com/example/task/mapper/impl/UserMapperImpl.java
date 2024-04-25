package com.example.task.mapper.impl;

import com.example.task.entity.User;
import com.example.task.mapper.UserMapper;
import com.example.task.model.dto.CustomUserDetails;
import com.example.task.model.request.RegisterRequest;
import com.example.task.model.response.LoginResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerRequestToUser(RegisterRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .isEnabled(false)
                .confirmationCode(UUID.randomUUID().toString())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    @Override
    public LoginResponse toLoginResponse(String accessToken,String refreshToken,User user){
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }



    @Override
    public CustomUserDetails userToCustomUserDetails(User user){
        return  CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .isEnabled(user.getIsEnabled())
                .password(user.getPassword())
                .build();
    }

}
