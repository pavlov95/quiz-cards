package com.quizCards.demo.web.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(min=4, max=20)
    private String username;
    @Size(min=4, max=20)
    private String password;

    @Email
    private String email;
}
