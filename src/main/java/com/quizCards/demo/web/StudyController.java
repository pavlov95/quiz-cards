package com.quizCards.demo.web;

import com.quizCards.demo.entities.Card;
import com.quizCards.demo.services.CardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("decks/{deckId}/study")
public class StudyController {
    private final CardService cardService;


    public StudyController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping()
    public ModelAndView getStudyPage(@PathVariable UUID deckId
            ,@RequestParam(defaultValue = "all") String type
            , HttpSession httpSession){

        UUID userId = (UUID) httpSession.getAttribute("user_id");

        List<Card> cards = cardService.getCardsForStudy(deckId, type, userId);

        ModelAndView modelAndView = new ModelAndView("study");
        modelAndView.addObject("deckId", deckId);
        modelAndView.addObject("type", type);
        modelAndView.addObject("cards", cards);

        return modelAndView;
    }


}
