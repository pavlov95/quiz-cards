package com.quizCards.demo.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    private String link;

    @ManyToOne
    private Deck deck;

    private boolean isDifficult;

    private LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    //ToDo add field like
}
