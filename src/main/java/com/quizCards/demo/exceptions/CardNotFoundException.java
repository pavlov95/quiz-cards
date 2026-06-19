package com.quizCards.demo.exceptions;

import java.util.UUID;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(UUID cardId) {
        super("Card with id " + cardId + " not found");
    }
}
