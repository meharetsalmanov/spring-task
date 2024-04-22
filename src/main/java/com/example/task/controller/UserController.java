package com.example.task.controller;

import com.example.task.model.request.LoginRequest;
import com.example.task.model.request.RegisterRequest;
import com.example.task.model.response.LoginResponse;
import com.example.task.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request)
    {
        userService.register(request);
    }

    @GetMapping("/confirm")
    public void userConfirmation(String code){
        userService.userConfirmation(code);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }
}
