package com.roshan.know_base.ai.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConversationResponse(
        UUID id,
        String title,
        LocalDateTime createdAt
) {
}
