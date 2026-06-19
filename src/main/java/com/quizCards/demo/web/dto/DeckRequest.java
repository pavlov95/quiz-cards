package com.quizCards.demo.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeckRequest {
    @Size(min=3, max=50)
    private String name;
    @Size(min=3, max=500)
    private String description;
    @NotNull
    private Boolean isPublic;
}
