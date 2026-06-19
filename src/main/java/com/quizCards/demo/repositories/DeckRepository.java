package com.quizCards.demo.repositories;

import com.quizCards.demo.entities.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface DeckRepository extends JpaRepository<Deck, UUID> {
    List<Deck> findAllByCreatedById(UUID userId);

    @Query("""
        SELECT d
        FROM Deck d
        WHERE d.isPublic = true
        AND d.createdBy.id <> :userId
        ORDER BY function('RAND')
    """)
    List<Deck> findRandomPublicDecksExcludingUser(@Param("userId") UUID userId, Pageable pageable);

}
