package com.quizCards.demo.exceptions;

import java.util.UUID;

public class EmailNotAvailableException extends RuntimeException  {
    public EmailNotAvailableException(String email) {
        super("Email " + email + " already exists.");
    }
}
