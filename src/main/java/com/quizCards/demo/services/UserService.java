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

    public void register( RegisterRequest registerRequest) {
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new RuntimeException("No such user"));
        String rawPassword = loginRequest.getPassword();
        String encodedPassword = user.getPassword();
        if(!passwordEncoder.matches(rawPassword, encodedPassword)){
            throw new RuntimeException("Username and password do not match");
        }
        return user;
    }

    public User findUserById(UUID userId){
        return userRepository.findById(userId).get();
    }
}
