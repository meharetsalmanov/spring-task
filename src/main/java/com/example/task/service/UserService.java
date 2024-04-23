package com.example.task.service;

import com.example.task.entity.User;
import com.example.task.exception.BaseException;
import com.example.task.model.request.ChangePasswordRequest;
import com.example.task.model.request.LoginRequest;
import com.example.task.model.request.RegisterRequest;
import com.example.task.model.response.LoginResponse;
import com.example.task.repository.UserRepository;
import com.example.task.service.security.AccessTokenManager;
import com.example.task.service.security.ChangePasswordManager;
import com.example.task.service.security.RefreshTokenManager;
import com.example.task.util.JwtUtil;
import com.example.task.util.MailContentBuilder;
import com.example.task.util.MailSenderUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.example.task.model.enums.response.ErrorResponseMessages.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderUtil mailSenderUtil;
    private final MailContentBuilder mailContentBuilder;
    private final AuthenticationManager authenticationManager;
    private final AccessTokenManager accessTokenManager;
    private final RefreshTokenManager refreshTokenManager;
    private final ChangePasswordManager changePasswordManager;


    public void register(RegisterRequest request){

        Optional<User> user = userRepository.findByEmailOrUsername(request.getEmail(),request.getUsername());

        if (user.isPresent())
            throw BaseException.of(ALREADY_REGISTERED);

        User userEntity = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(false)
                .confirmationCode(UUID.randomUUID().toString())
                .build();

        try{
            mailSenderUtil.sendMail(mailContentBuilder.confirmationMailDto(userEntity));
        } catch (MessagingException e) {
            throw new MailSendException("Error in sending mail");
        }

        userRepository.save(userEntity);
    }

    public void userConfirmation(String code){
        User user = userRepository.findByConfirmationCode(code).orElseThrow(()->
                BaseException.of(USER_CONFIRMATION_CODE_NOT_FOUND)
        );
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername()).orElseThrow(
                () -> BaseException.of(USER_NOT_FOUND)
        );

        if(!user.getIsEnabled()) throw BaseException.of(USER_NOT_CONFIRMED);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        return LoginResponse.builder()
                .accessToken(accessTokenManager.generate(user))
                .refreshToken(refreshTokenManager.generate(user))
                .user(LoginResponse.UserInfoDetails.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .build())
                .build();
    }


    public void forgotPassword(String username){
        User user = userRepository.findByEmailOrUsername(username, username).orElseThrow(
                () -> BaseException.of(USER_NOT_FOUND)
        );

        if(!user.getIsEnabled()) throw BaseException.of(USER_NOT_CONFIRMED);

        String token = changePasswordManager.getToken(user);

        try{
            mailSenderUtil.sendMail(mailContentBuilder.forgotPasswordMailDto(user,token));
        } catch (MessagingException e) {
            throw new MailSendException("Error in sending mail");
        }

    }

    public void changePassword(ChangePasswordRequest request)
    {
        String email = changePasswordManager.getEmailFromToken(request.getCode());
        User user = userRepository.findByEmail(email).orElseThrow(()->BaseException.of(USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

}
