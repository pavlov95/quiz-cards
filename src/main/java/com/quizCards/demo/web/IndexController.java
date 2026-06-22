package com.quizCards.demo.web;

import com.quizCards.demo.entities.Deck;
import com.quizCards.demo.entities.User;
import com.quizCards.demo.services.DeckService;
import com.quizCards.demo.services.UserService;
import com.quizCards.demo.web.dto.LoginRequest;
import com.quizCards.demo.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {
    private final UserService userService;
    private final DeckService deckService;

    public IndexController(UserService userService, DeckService deckService) {
        this.userService = userService;
        this.deckService = deckService;
    }


    @GetMapping()
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerRequest", new RegisterRequest());

        return modelAndView;
    }

    @PostMapping("/register")
    public String register(@Valid RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.register(registerRequest);

        return "redirect:/login";

    }

    @GetMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginRequest", new LoginRequest());
        return modelAndView;
    }

    @PostMapping("/login")
    public String login(LoginRequest loginRequest, HttpSession httpSession) {
        User user = userService.login(loginRequest);
        httpSession.setAttribute("user_id", user.getId());
        return "redirect:/home";
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession httpSession) {
        UUID userId = (UUID) httpSession.getAttribute("user_id");
        ModelAndView modelAndView = new ModelAndView("home");

        List<Deck> myDecks = deckService.getDecksByUser(userId);
        modelAndView.addObject("myDecks", myDecks);
        List<Deck> randomDecks = deckService.getRandomDecksNotByUser(userId, 3);
        modelAndView.addObject("randomDecks", randomDecks);

        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
