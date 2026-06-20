package com.quizCards.demo.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    //Search how this be shown to user
    @Size(min=3, max=150)
    @NotBlank
    private String question;

    @Size(min=1, max=150)
    private String answer;


    private String link;

    @NotNull(message = "Please choose if the card is difficult")
    private Boolean isDifficult;
}
