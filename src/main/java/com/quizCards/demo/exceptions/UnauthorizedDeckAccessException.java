package com.quizCards.demo.exceptions;

public class UnauthorizedDeckAccessException extends RuntimeException {
    public UnauthorizedDeckAccessException() {
        super("You are not allowed to access this deck.");
    }
}
