package com.example.task.model.enums.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorResponseMessages implements ResponseMessages {
    UNEXPECTED("unexpected", "Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND("not_found_%s", "%s can't find %s", HttpStatus.NOT_FOUND),
    ALREADY_REGISTERED("already_registered", "User with username or email already exists", HttpStatus.CONFLICT),
    PHONE_NUMBER_ALREADY_EXIST("phone_number_already_exist", "Phone number already exist", HttpStatus.CONFLICT),
    FORBIDDEN("forbidden", "Forbidden", HttpStatus.FORBIDDEN),
    USER_NOT_ACTIVE("user_not_active", "User is not active", HttpStatus.FORBIDDEN),
    OTP_IS_NOT_VALID("otp_is_not_valid", "OTP is not valid", HttpStatus.CONFLICT),
    USER_NOT_FOUND("user_not_found", "User with username or email not exists", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("category_not_found", "Category not exists", HttpStatus.NOT_FOUND),
    TASK_NOT_FOUND("task_not_found", "Task not exists", HttpStatus.NOT_FOUND),
    USER_CONFIRMATION_CODE_NOT_FOUND("user_confirmation_code_not_found", "User with this confirm code was not found!", HttpStatus.NOT_FOUND),
    USER_NOT_CONFIRMED("user_not_confirmed", "User mail is not confirmed", HttpStatus.CONFLICT),
    INVALID_TOKEN("invalid_token", "Invalid token", HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED("token_expired", "Token expired", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("unauthorized", "unauthorized", HttpStatus.UNAUTHORIZED);


    String key;
    String message;
    HttpStatus status;

    @Override
    public String key() {
        return key;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}
