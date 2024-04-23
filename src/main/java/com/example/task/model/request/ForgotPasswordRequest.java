package com.example.task.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class ForgotPasswordRequest {

    @NotBlank(message = "Username or email should not be blank")
    private String username;
}
