package com.roshan.know_base.ai.service;

import reactor.core.publisher.Flux;

import java.util.UUID;

public interface RagService {
    Flux<String> processChat(UUID conversationId, String userQuestion);
}
