package com.example.task.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class RegisterRequest {

    @NotBlank( message = "Username should not be blank")
    private String username;

    @NotBlank(message = "Email should not be blank")
    @Email(message = "This field should be email")
    private String email;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 8,message = "Password should be contains at least 8 characters")
    private String password;
}
