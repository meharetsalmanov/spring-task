package com.example.task.util;

import com.example.task.entity.User;
import com.example.task.model.dto.MailDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailContentBuilder {

    @Value("${spring.site-url}")
    private String siteUrl;


    public  MailDto confirmationMailDto(User user){

        return MailDto.builder()
                .to(user.getEmail())
                .subject("Complete your registration")
                .body("Click the link to complete your registration. <br>" +
                        "<a> href=\"" + siteUrl + "/api/user/confirm?code=" + user.getConfirmationCode() + "\" Confirm </a>"
                        )
                .build();
    }

    public  MailDto forgotPasswordMailDto(User user,String token){

        return MailDto.builder()
                .to(user.getEmail())
                .subject("Reset your password")
                .body("Please click the link below to reset your password: <br>" +
                        "<a> href=" + siteUrl + "/api/user/change-password?code=" + token + " Confirm </a>"
                        )
                .build();
    }

}
