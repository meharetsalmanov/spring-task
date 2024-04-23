package com.example.task.model.response.base;

import com.example.task.exception.BaseException;
import com.example.task.model.enums.response.BaseResponseType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse <T> {
    private BaseResponseType type;
    private String message;
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .type(BaseResponseType.SUCCESS)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> created(T data) {
        return BaseResponse.<T>builder()
                .type(BaseResponseType.SUCCESS)
                .data(data)
                .build();
    }
    public static <T> BaseResponse<T> created() {
        return created(null);
    }
    public static BaseResponse<?> error(BaseException ex) {
        return error(ex.getResponseMessage().message());
    }

    public static BaseResponse<?> error(Exception ex) {
        return error(ex.getMessage());
    }

    public static BaseResponse<?> error(String message){
        return BaseResponse.builder()
                .type(BaseResponseType.ERROR)
                .message(message)
                .build();
    }

}
