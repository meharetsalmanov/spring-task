package com.example.task.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class ChangePasswordRequest {

    @NotBlank(message = "Code should not be blank")
    private String code;

    @NotBlank(message = "Password should not be blank")
    @Size(min = 8,message = "Password should be contains at least 8 characters")
    private String password;

}
