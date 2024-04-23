package com.example.task.model.enums.response;

import lombok.Getter;

@Getter
public enum BaseResponseType {
    ERROR("ERROR"),
    SUCCESS("SUCCESS");

    private final String value;

    BaseResponseType(String value) {
        this.value = value;
    }

}
