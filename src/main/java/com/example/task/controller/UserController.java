package com.example.task.controller;

import com.example.task.model.request.ChangePasswordRequest;
import com.example.task.model.request.ForgotPasswordRequest;
import com.example.task.model.request.LoginRequest;
import com.example.task.model.request.RegisterRequest;
import com.example.task.model.response.base.BaseResponse;
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
    public BaseResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return BaseResponse.success(userService.login(loginRequest));
    }

    @PostMapping("/forgot-password")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequest request){
        userService.forgotPassword(request.getUsername());
    }

    @PostMapping("/change-password")
    public BaseResponse<Object> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        userService.changePassword(request);
        return BaseResponse.success();
    }


}
