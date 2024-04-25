package com.example.task.service;

import com.example.task.entity.User;
import com.example.task.exception.BaseException;
import com.example.task.mapper.UserMapper;
import com.example.task.model.request.ChangePasswordRequest;
import com.example.task.model.request.LoginRequest;
import com.example.task.model.request.RegisterRequest;
import com.example.task.model.response.LoginResponse;
import com.example.task.repository.UserRepository;
import com.example.task.service.security.AccessTokenManager;
import com.example.task.service.security.ChangePasswordManager;
import com.example.task.service.security.RefreshTokenManager;
import com.example.task.util.MailContentBuilder;
import com.example.task.util.MailSenderUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

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
    private final UserMapper userMapper;


    public void register(RegisterRequest request) throws MessagingException {

        Optional<User> user = userRepository.findByEmailOrUsername(request.getEmail(),request.getUsername());

        if (user.isPresent())
            throw BaseException.of(ALREADY_REGISTERED);

        User userEntity = userMapper.registerRequestToUser(request);
        mailSenderUtil.sendMail(mailContentBuilder.confirmationMailDto(userEntity));
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

        return userMapper.toLoginResponse(
                accessTokenManager.generate(user),
                refreshTokenManager.generate(user),
                user
        );
    }


    public void forgotPassword(String username) throws MessagingException {
        User user = userRepository.findByEmailOrUsername(username, username).orElseThrow(
                () -> BaseException.of(USER_NOT_FOUND)
        );

        if(!user.getIsEnabled()) throw BaseException.of(USER_NOT_CONFIRMED);

        String token = changePasswordManager.getToken(user);
        mailSenderUtil.sendMail(mailContentBuilder.forgotPasswordMailDto(user,token));
    }

    public void changePassword(ChangePasswordRequest request)
    {
        String email = changePasswordManager.getEmailFromToken(request.getCode());
        User user = userRepository.findByEmail(email).orElseThrow(()->BaseException.of(USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

}
