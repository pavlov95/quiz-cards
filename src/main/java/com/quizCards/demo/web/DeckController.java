package com.quizCards.demo.web;

import com.quizCards.demo.entities.Deck;
import com.quizCards.demo.services.DeckService;
import com.quizCards.demo.web.dto.DeckRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/decks")
public class DeckController {
    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/create")
    public ModelAndView getDecksPage() {


        ModelAndView modelAndView = new ModelAndView("deck-create");
        modelAndView.addObject("deckRequest", new DeckRequest());

        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView createDeck(@Valid DeckRequest deckRequest, BindingResult bindingResult, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("deck-create");
        }
        UUID userId = (UUID) httpSession.getAttribute("user_id");
        Deck deck = deckService.createDeck(deckRequest, userId);

        return new ModelAndView("redirect:/decks/" + deck.getId());
    }

    @GetMapping("/{id}")
    public ModelAndView getDeck(@PathVariable UUID id, HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");

        Deck deck = deckService.getDeckById(id);

        boolean isOwner = deck.getCreatedBy().getId().equals(userId);

        ModelAndView modelAndView = new ModelAndView("deck-details");
        modelAndView.addObject("deck", deck);
        modelAndView.addObject("isOwner", isOwner);

        return modelAndView;
    }

    @PutMapping("/{id}")
    public ModelAndView editDeck(@PathVariable UUID id,
                                 @Valid DeckRequest deckRequest,
                                 BindingResult bindingResult,
                                 HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("deck-edit");
            modelAndView.addObject("deckId", id);
            return modelAndView;
        }

        UUID userId = (UUID) httpSession.getAttribute("user_id");
        deckService.editDeck(deckRequest, id, userId);
        return new ModelAndView("redirect:/decks/" + id);
    }

    @DeleteMapping("/{id}")
    public String deleteDeck(@PathVariable("id") UUID deckId,
                             HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");

        deckService.deleteDeck(deckId, userId);

        return "redirect:/home";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView getEditDeckPage(@PathVariable UUID id, HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");

        DeckRequest deckRequest = deckService.getDeckRequestForEdit(id, userId);

        ModelAndView modelAndView = new ModelAndView("deck-edit");
        modelAndView.addObject("deckRequest", deckRequest);
        modelAndView.addObject("deckId", id);

        return modelAndView;
    }




}


