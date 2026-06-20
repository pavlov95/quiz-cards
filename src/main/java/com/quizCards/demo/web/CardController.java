package com.quizCards.demo.web;

import com.quizCards.demo.services.CardService;
import com.quizCards.demo.web.dto.CardRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/decks/{deckId}/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/create")
    public ModelAndView getCardsPage(@PathVariable UUID deckId,
                                     HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");

        cardService.validateDeckOwner(deckId, userId);

        ModelAndView modelAndView = new ModelAndView("cards-create");
        modelAndView.addObject("cardRequest", new CardRequest());
        modelAndView.addObject("deckId", deckId);

        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView postCard(@PathVariable UUID deckId, @Valid CardRequest cardRequest, BindingResult bindingResult
            , HttpSession httpSession, @RequestParam String action) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("cards-create");
            modelAndView.addObject("deckId", deckId);
            return modelAndView;
        }
        UUID userId = (UUID) httpSession.getAttribute("user_id");

        cardService.createCard(cardRequest, deckId, userId);

        if (action.equals("save-and-add-another")) {
            return new ModelAndView("redirect:/decks/" + deckId + "/cards/create");
        }

        return new ModelAndView("redirect:/decks/" + deckId);
    }

    @GetMapping("/{cardId}")
    public ModelAndView getCardEditPage(@PathVariable UUID deckId, @PathVariable UUID cardId
            , HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");

        CardRequest cardRequest = cardService.getCardRequestForEdit(cardId, deckId, userId);

        ModelAndView modelAndView = new ModelAndView("card-edit");
        modelAndView.addObject("cardRequest", cardRequest);
        modelAndView.addObject("deckId", deckId);
        modelAndView.addObject("cardId", cardId);

        return modelAndView;
    }

    @PutMapping("/{cardId}")
    public ModelAndView editCard(@PathVariable UUID deckId, @PathVariable UUID cardId, @Valid CardRequest cardRequest
            , BindingResult bindingResult, HttpSession httpSession) {

        if(bindingResult.hasErrors()){
            ModelAndView modelAndView = new ModelAndView("card-edit");
            modelAndView.addObject("deckId" , deckId);
            modelAndView.addObject("cardId", cardId);
            return modelAndView;
        }
        UUID userId = (UUID) httpSession.getAttribute("user_id");

        cardService.editCard(cardRequest, cardId, deckId, userId);

        return new ModelAndView("redirect:/decks/" + deckId);
    }

    @DeleteMapping("/{cardId}")
    public ModelAndView deleteCard(@PathVariable UUID deckId, @PathVariable UUID cardId, HttpSession httpSession){
        UUID userId = (UUID) httpSession.getAttribute("user_id");
        cardService.deleteCard(cardId, deckId, userId);
        return new ModelAndView("redirect:/decks/" + deckId);
    }

}
