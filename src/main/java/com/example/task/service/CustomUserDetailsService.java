package com.example.task.service;

import com.example.task.entity.User;
import com.example.task.model.dto.CustomUserDetails;
import com.example.task.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailOrUsername(username,username).map(user -> CustomUserDetails.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .isEnabled(user.getIsEnabled())
                    .password(user.getPassword())
                    .build()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
