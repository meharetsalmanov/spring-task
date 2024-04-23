package com.example.task.exception;

import com.example.task.model.enums.response.ResponseMessages;
import lombok.*;
import static com.example.task.model.enums.response.ErrorResponseMessages.UNEXPECTED;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BaseException extends RuntimeException {

    private ResponseMessages responseMessage;

    @Override
    public String getMessage() {
        return responseMessage.message();
    }

    public static BaseException of(ResponseMessages responseMessage) {
        return BaseException.builder().responseMessage(responseMessage).build();
    }

    public static BaseException unexpected() {
        return of(UNEXPECTED);
    }

    public static BaseException of(Exception e){
        return new BaseException();
    }


}