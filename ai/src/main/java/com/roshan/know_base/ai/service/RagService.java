package com.roshan.know_base.ai.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface RagService {
    SseEmitter processChat(UUID conversationId, String userQuestion);
}
