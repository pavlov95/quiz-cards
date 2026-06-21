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

    public List<Card> findAllCardsByDeckId(UUID deckId) {
        return cardRepository.findAllByDeckId(deckId);
    }

    @Transactional
    public void createCard(CardRequest cardRequest, UUID deckId, UUID userId) {
        Deck deck = deckService.getDeckById(deckId);

        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedDeckAccessException();
        }
        Card card = Card.builder()
                .question(cardRequest.getQuestion())
                .answer(cardRequest.getAnswer())
                .link(cardRequest.getLink())
                .deck(deck)
                .isDifficult(cardRequest.getIsDifficult())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        deckService.updateDeck(deck);
        cardRepository.save(card);

    }
    public CardRequest getCardRequestForEdit(UUID cardId, UUID deckId, UUID userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        Deck deck = card.getDeck();

        if (!deck.getId().equals(deckId)) {
            throw new CardNotFoundException(cardId);
        }

        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedDeckAccessException();
        }

        CardRequest cardRequest = new CardRequest();
        cardRequest.setQuestion(card.getQuestion());
        cardRequest.setAnswer(card.getAnswer());
        cardRequest.setLink(card.getLink());
        cardRequest.setIsDifficult(card.isDifficult());

        return cardRequest;
    }

    @Transactional
    public void editCard(CardRequest cardRequest, UUID cardId, UUID deckId, UUID userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        Deck deck = card.getDeck();

        if (!deck.getId().equals(deckId)) {
            throw new CardNotFoundException(cardId);
        }

        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedDeckAccessException();
        }

        card.setQuestion(cardRequest.getQuestion());
        card.setAnswer(cardRequest.getAnswer());
        card.setLink(cardRequest.getLink());
        card.setDifficult(cardRequest.getIsDifficult());
        card.setUpdatedOn(LocalDateTime.now());

        cardRepository.save(card);
        deckService.updateDeck(deck);
    }

    @Transactional
    public void deleteCard(UUID cardId, UUID deckId, UUID userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        Deck deck = card.getDeck();

        if (!deck.getId().equals(deckId)) {
            throw new CardNotFoundException(cardId);
        }

        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedDeckAccessException();
        }

        cardRepository.delete(card);
        deckService.updateDeck(deck);
    }

    public List<Card> getCardsForStudy(UUID deckId, String type, UUID userId) {
        Deck deck = deckService.getDeckById(deckId);

        if (!deck.isPublic() && !deck.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedDeckAccessException();
        }

        List<Card> cards = cardRepository.findAllCardsByDeckId(deckId);

        return switch (type) {
            case "difficult" -> cards.stream()
                    .filter(Card::isDifficult)
                    .toList();

            case "normal" -> cards.stream()
                    .filter(card -> !card.isDifficult())
                    .toList();

            case "all" -> cards;

            default -> throw new IllegalArgumentException("Invalid study type: " + type);
        };
    }

    public void validateDeckOwner(UUID deckId, UUID userId) {
        Deck deck = deckService.getDeckById(deckId);

        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedDeckAccessException();
        }
    }
    //ToDo Add Study/View Only one card option

    //ToDo Add options to get most liked decks
}
