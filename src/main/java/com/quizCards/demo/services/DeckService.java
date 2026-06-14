package com.quizCards.demo.services;

import com.quizCards.demo.entities.Deck;
import com.quizCards.demo.entities.User;
import com.quizCards.demo.exceptions.DeckNotFoundException;
import com.quizCards.demo.exceptions.UnauthorizedDeckAccessException;
import com.quizCards.demo.repositories.DeckRepository;
import com.quizCards.demo.web.dto.DeckRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private final UserService userService;

    public DeckService(DeckRepository deckRepository, UserService userService) {
        this.deckRepository = deckRepository;
        this.userService = userService;
    }


}
