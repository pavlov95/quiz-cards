package com.quizCards.demo.services;

import com.quizCards.demo.entities.Role;
import com.quizCards.demo.entities.User;
import com.quizCards.demo.exceptions.EmailNotAvailableException;
import com.quizCards.demo.exceptions.UserNotFoundException;
import com.quizCards.demo.exceptions.UsernameNotAvailableException;
import com.quizCards.demo.exceptions.UsernamePasswordMismatchException;
import com.quizCards.demo.repositories.UserRepository;
import com.quizCards.demo.web.dto.LoginRequest;
import com.quizCards.demo.web.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        Optional<User> byUsername = userRepository.findByUsername(username);
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byUsername.isPresent()){
            throw new UsernameNotAvailableException(username);
        } else if(byEmail.isPresent()){
            throw new EmailNotAvailableException(email);
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(email)
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(()-> new UserNotFoundException("No such user"));
        String rawPassword = loginRequest.getPassword();
        String encodedPassword = user.getPassword();
        if(!passwordEncoder.matches(rawPassword, encodedPassword)){
            throw new UsernamePasswordMismatchException();
        }
        return user;
    }
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public User findUserById(UUID userId){
        return userRepository.findById(userId).get();
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
