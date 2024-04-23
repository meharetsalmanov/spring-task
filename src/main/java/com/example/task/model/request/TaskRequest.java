package com.example.task.model.request;

import com.example.task.model.enums.TaskPriority;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskRequest {

    @NotBlank(message = "Task name is required")
    private String name;

    private String description;

    private TaskPriority priority;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime deadline;

    private Long categoryId;

}
