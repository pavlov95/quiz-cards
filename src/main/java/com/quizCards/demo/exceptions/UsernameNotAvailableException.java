package com.quizCards.demo.exceptions;

public class UsernameNotAvailableException extends RuntimeException {
    public UsernameNotAvailableException(String username) {
        super(username + " is already taken");
    }
}
