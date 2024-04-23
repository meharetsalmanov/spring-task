package com.example.task.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private Boolean isEnabled;

    private String confirmationCode;
}
