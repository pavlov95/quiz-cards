package com.quizCards.demo.services;

import com.quizCards.demo.entities.Deck;
import com.quizCards.demo.entities.Role;
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

    public List<Deck> getDecksByUser(UUID userId){
        return deckRepository.findAllByCreatedById(userId);
    }

    public List<Deck> getRandomDecksNotByUser(UUID userId, int count) {
       return deckRepository.findRandomPublicDecksExcludingUser(userId, PageRequest.of(0, count));

    }


    public Deck createDeck(DeckRequest deckRequest, UUID userId) {
        User user = userService.findUserById(userId);

        Deck deck = Deck.builder()
                .name(deckRequest.getName())
                .description(deckRequest.getDescription())
                .isPublic(deckRequest.getIsPublic())
                .createdBy(user)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        return deckRepository.save(deck);
    }

    public Deck getDeckById(UUID id) {
         return deckRepository.findById(id).orElseThrow(UnauthorizedDeckAccessException::new);
    }

    @Transactional
    public void deleteDeck(UUID deckId, UUID userId) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(DeckNotFoundException::new);
        User user = userService.findUserById(userId);
        validateDeletePermission(deck, user);

        deckRepository.delete(deck);
    }

    public void editDeck(DeckRequest deckRequest, UUID deckId, UUID userId) {
        Deck deck = deckRepository.findById(deckId).orElseThrow(DeckNotFoundException::new);
        if(!deck.getCreatedBy().getId().equals(userId)){
           throw new UnauthorizedDeckAccessException();
        }
        deck.setName(deckRequest.getName());
        deck.setPublic(deckRequest.getIsPublic());
        deck.setDescription(deck.getDescription());
        deck.setUpdatedOn(LocalDateTime.now());
        deckRepository.save(deck);
    }

    public DeckRequest getDeckRequestForEdit(UUID deckId, UUID userId) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(DeckNotFoundException::new);

        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedDeckAccessException();
        }

        DeckRequest deckRequest = new DeckRequest();
        deckRequest.setName(deck.getName());
        deckRequest.setDescription(deck.getDescription());
        deckRequest.setIsPublic(deck.isPublic());

        return deckRequest;
    }

    public void updateDeck(Deck deck){
        deck.setUpdatedOn(LocalDateTime.now());
        deckRepository.save(deck);
    }

    private void validateDeletePermission(Deck deck, User user) {
        boolean isOwner = deck.getCreatedBy()
                .getId()
                .equals(user.getId());

        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean adminCanDelete = isAdmin && deck.isPublic();

        if (!isOwner && !adminCanDelete) {
            throw new UnauthorizedDeckAccessException();
        }
    }
}
