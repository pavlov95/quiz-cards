package com.quizCards.demo.exceptions;

import java.util.UUID;

public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(UUID deckId) {
        super("Deck with id " + deckId + " not found.");
    }
}
