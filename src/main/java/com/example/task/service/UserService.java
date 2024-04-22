package com.example.task.service;

import com.example.task.entity.User;
import com.example.task.model.request.LoginRequest;
import com.example.task.model.request.RegisterRequest;
import com.example.task.model.response.LoginResponse;
import com.example.task.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderUtil mailSenderUtil;
    private final MailContentBuilder mailContentBuilder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    public void register(RegisterRequest request){

        Optional<User> user = userRepository.findByEmailOrUsername(request.getEmail(),request.getUsername());

        if (user.isPresent())
            throw new EntityExistsException("User with username or email already exists");

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
            new EntityNotFoundException(String.format(
                   "User with this confirm code [%s] was not found!", code
           ))
        );

        user.setIsEnabled(true);
        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        System.out.println(request.getUsername());
        User user = userRepository.findByEmailOrUsername(request.getUsername(), request.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User with username or email not exists")
        );

        System.out.println(user.getEmail());

        if(!user.isEnabled()) throw new DisabledException("User mail is not confirmed");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        return LoginResponse.builder()
                .accessToken(jwtUtil.accessToken(user))
                .refreshToken(jwtUtil.refreshToken(user))
                .userDetails(LoginResponse.UserDetails.builder()
                        .email(user.getEmail())
                        .id(user.getId())
                        .build())
                .build();

    }

}
