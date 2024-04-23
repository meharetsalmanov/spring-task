package com.example.task.model.enums;

import lombok.Getter;

@Getter
public enum TaskPriority {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int value;

    TaskPriority(int value) {
        this.value = value;
    }

}
