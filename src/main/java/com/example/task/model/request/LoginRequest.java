package com.example.task.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class LoginRequest {

    @NotBlank(message = "Username or mail should not be blank")
    private String username;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 8,message = "Password should be contains at least 8 characters")
    private String password;

}
