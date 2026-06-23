package com.quizCards.demo.web;

import com.quizCards.demo.entities.Deck;
import com.quizCards.demo.entities.User;
import com.quizCards.demo.security.SessionInterceptor;
import com.quizCards.demo.services.DeckService;
import com.quizCards.demo.services.UserService;
import com.quizCards.demo.web.dto.LoginRequest;
import com.quizCards.demo.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private DeckService deckService;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<RegisterRequest> registerRequestArgumentCaptor;

    @Test
    void getIndexEndpoint_shouldReturn200OkStatusAndIndexView() throws Exception {
        MockHttpServletRequestBuilder httpRequest = get("/");

        mockMvc.perform(httpRequest)
                .andExpect(view().name("index"))
                .andExpect(status().isOk());
    }

    @Test
    void postRegister_shouldReturn302RedirectAndRedirectToLoginPageAndInvokeRegisterServiceMethod() throws Exception {
        MockHttpServletRequestBuilder httpRequest = post("/register")
                .formField("username", "asdasd")
                .formField("password", "asdasdasd")
                .formField("email", "asd@asdasd.bg");

        mockMvc.perform(httpRequest)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).register(registerRequestArgumentCaptor.capture());
        RegisterRequest dto = registerRequestArgumentCaptor.getValue();
        assertEquals("asdasd", dto.getUsername());
        assertEquals("asdasdasd", dto.getPassword());
        assertEquals("asd@asdasd.bg", dto.getEmail());
    }

    @Test
    void getHome_shouldReturnHomePageForAuthenticatedUser() throws Exception {
        UUID userId = UUID.randomUUID();
        List<Deck> myDecks = List.of();
        List<Deck> randomDecks = List.of();

        when(deckService.getDecksByUser(userId)).thenReturn(myDecks);
        when(deckService.getRandomDecksNotByUser(userId, 3)).thenReturn(randomDecks);

        mockMvc.perform(get("/home")
                        .sessionAttr(SessionInterceptor.USER_ID_FROM_SESSION, userId))
                .andExpect(status().isOk()).andExpect(view().name("home"))
                .andExpect(model().attribute("myDecks", myDecks))
                .andExpect(model().attribute("randomDecks", randomDecks))
                .andExpect(model().attribute("userId", userId));
        verify(deckService).getDecksByUser(userId);
        verify(deckService).getRandomDecksNotByUser(userId, 3);
    }

    @Test
    void getHome_withoutSession_shouldRedirectToLandingPage() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void postLogin_shouldCreateSessionAndRedirectToHome() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("test-user")
                .build();

        when(userService.login(any(LoginRequest.class))).thenReturn(user);

        mockMvc.perform(post("/login")
                        .formField("username", "test-user")
                        .formField("password", "Password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(request().sessionAttribute(SessionInterceptor.USER_ID_FROM_SESSION, userId));
    }

    @Test
    void postLogout_shouldInvalidateSessionAndRedirectToIndex() throws Exception {
        UUID userId = UUID.randomUUID();
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionInterceptor.USER_ID_FROM_SESSION, userId);
        mockMvc.perform(post("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        assertTrue(session.isInvalid());
    }
}
