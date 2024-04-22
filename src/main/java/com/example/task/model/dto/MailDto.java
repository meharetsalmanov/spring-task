package com.example.task.model.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MailDto {
    private String to;
    private String subject;
    private String body;
}
