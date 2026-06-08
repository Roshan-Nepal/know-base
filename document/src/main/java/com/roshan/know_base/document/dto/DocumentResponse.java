package com.roshan.know_base.document.dto;

import com.roshan.know_base.document.entity.DocumentContent;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String name,
        String type,
        String status,
        Long fileSize,
        Map<String, Object> metaData,
        LocalDateTime createdAt,
        String content
){}
