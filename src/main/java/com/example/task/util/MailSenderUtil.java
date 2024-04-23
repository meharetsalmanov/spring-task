package com.example.task.util;

import com.example.task.model.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class MailSenderUtil {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Async
    public void sendMail(MailDto mailDto) throws MessagingException {


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(from);
        mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(mailDto.getTo()));
        mimeMessage.setSubject(mailDto.getSubject());
        mimeMessage.setContent(mailDto.getBody(), "text/html; charset=utf-8");
        javaMailSender.send(mimeMessage);
    }

}
