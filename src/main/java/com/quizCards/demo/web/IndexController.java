package com.quizCards.demo.web;


import com.quizCards.demo.services.DeckService;
import com.quizCards.demo.services.UserService;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.servlet.ModelAndView;



@Controller
public class IndexController {
    private final UserService userService;
    private final DeckService deckService;

    public IndexController(UserService userService, DeckService deckService) {
        this.userService = userService;
        this.deckService = deckService;
    }


    @GetMapping()
    public ModelAndView getIndexPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        return modelAndView;
    }




}
