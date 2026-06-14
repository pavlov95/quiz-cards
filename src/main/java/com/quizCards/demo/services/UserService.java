package com.quizCards.demo.services;

import com.quizCards.demo.entities.Role;
import com.quizCards.demo.entities.User;
import com.quizCards.demo.repositories.UserRepository;
import com.quizCards.demo.web.dto.LoginRequest;
import com.quizCards.demo.web.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


}
