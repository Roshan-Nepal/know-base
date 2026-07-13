package com.roshan.know_base.ai.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChatRequest(
        UUID conversationId,
        @NotBlank(message = "Message cannot be blank.")
        String message
) {
}
