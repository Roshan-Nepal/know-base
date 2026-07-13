package com.roshan.know_base.ai.dto;

import com.roshan.know_base.ai.entity.MessageRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record MessageResponse(
        UUID id,
        MessageRole role,
        String content,
        List<UUID> sourceChunks,
        LocalDateTime createdAt
) {
}
