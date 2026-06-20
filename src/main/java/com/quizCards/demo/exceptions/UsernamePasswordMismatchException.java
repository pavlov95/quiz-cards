package com.quizCards.demo.exceptions;

public class UsernamePasswordMismatchException extends RuntimeException {
    public UsernamePasswordMismatchException() {
        super("Username and password do not match");
    }
}
