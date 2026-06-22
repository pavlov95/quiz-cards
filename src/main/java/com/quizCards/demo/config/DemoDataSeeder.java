package com.quizCards.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizCards.demo.entities.Card;
import com.quizCards.demo.entities.Deck;
import com.quizCards.demo.entities.Role;
import com.quizCards.demo.entities.User;
import com.quizCards.demo.services.CardService;
import com.quizCards.demo.services.DeckService;
import com.quizCards.demo.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.demo-data.enabled", havingValue = "true", matchIfMissing = true)
public class DemoDataSeeder implements ApplicationRunner {

    private static final String DEMO_USERNAME = "demo";
    private static final String DEMO_EMAIL = "demo@example.com";
    private static final String DEMO_PASSWORD = "Demo123!";

    private static final String DATA_FILE = "demo/demo-data.json";

    private final UserService userService;
    private final DeckService deckService;
    private final CardService cardService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public DemoDataSeeder(UserService userService, DeckService deckService, CardService cardService
            , PasswordEncoder passwordEncoder, ObjectMapper objectMapper) {
        this.userService = userService;
        this.deckService = deckService;
        this.cardService = cardService;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userService.existsByUsername(DEMO_USERNAME)) {
            return;
        }

        DemoDataDefinition demoData = readDemoData();

        User demoUser = createDemoUser();
        userService.updateUser(demoUser);

        for (DemoDataDefinition.DeckData deckData : demoData.decks()) {

            Deck deck = createDeck(deckData, demoUser);
            deckService.updateDeck(deck);

            List<Card> cards = createCards(deckData.cards(), deck);

            cardService.updateAll(cards);
        }
    }

    private DemoDataDefinition readDemoData() {
        ClassPathResource resource =
                new ClassPathResource(DATA_FILE);

        try (InputStream inputStream = resource.getInputStream()) {

            DemoDataDefinition data = objectMapper.readValue(inputStream,
                    DemoDataDefinition.class
            );

            if (data.decks() == null || data.decks().isEmpty()) {
                throw new IllegalStateException("The demo-data file contains no decks.");
            }

            return data;

        } catch (IOException exception) {
            throw new IllegalStateException("Could not read " + DATA_FILE, exception);
        }
    }

    private User createDemoUser() {
        return User.builder()
                .username(DEMO_USERNAME)
                .email(DEMO_EMAIL)
                .password(passwordEncoder.encode(DEMO_PASSWORD))
                .role(Role.ADMIN)
                .build();
    }

    private Deck createDeck(DemoDataDefinition.DeckData deckData, User demoUser) {
        return Deck.builder()
                .name(deckData.name())
                .description(deckData.description())
                .isPublic(deckData.publicDeck())
                .createdBy(demoUser)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    private List<Card> createCards(List<DemoDataDefinition.CardData> cardData, Deck deck) {
        if (cardData == null) {
            return List.of();
        }

        List<Card> cards = new ArrayList<>();

        for (DemoDataDefinition.CardData data : cardData) {
            cards.add(Card.builder()
                    .question(data.question())
                    .answer(data.answer())
                    .link(data.link())
                    .isDifficult(data.difficult())
                    .deck(deck)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build());
        }
        return cards;
    }
}