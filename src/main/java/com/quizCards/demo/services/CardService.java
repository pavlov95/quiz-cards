package com.quizCards.demo.services;

import com.quizCards.demo.entities.Card;
import com.quizCards.demo.entities.Deck;
import com.quizCards.demo.exceptions.CardNotFoundException;
import com.quizCards.demo.exceptions.UnauthorizedDeckAccessException;
import com.quizCards.demo.repositories.CardRepository;
import com.quizCards.demo.web.dto.CardRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final DeckService deckService;

    public CardService(CardRepository cardRepository, DeckService deckService) {
        this.cardRepository = cardRepository;
        this.deckService = deckService;
    }

}
