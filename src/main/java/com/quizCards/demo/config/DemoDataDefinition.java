package com.quizCards.demo.config;

import java.util.List;

public record DemoDataDefinition(List<DeckData> decks) {

    public record DeckData(
            String name,
            String description,
            boolean publicDeck,
            List<CardData> cards
    ) {
    }

    public record CardData(
            String question,
            String answer,
            String link,
            boolean difficult
    ) {
    }
}