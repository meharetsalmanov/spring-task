package com.example.task.mapper;

import com.example.task.entity.User;
import com.example.task.model.dto.CustomUserDetails;
import com.example.task.model.request.RegisterRequest;
import com.example.task.model.response.LoginResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User registerRequestToUser(RegisterRequest request);
    LoginResponse toLoginResponse(String accessToken, String refreshToken, User user);

    CustomUserDetails userToCustomUserDetails(User user);
}
