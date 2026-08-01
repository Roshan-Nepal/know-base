package com.roshan.know_base.document.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record DocumentDetailResponse (
        UUID id,
        String name,
        String type,
        String status,
        Long fileSize,
        LocalDateTime createdAt,
        String content,
        Set<TagResponse> tags
){
}
