package com.roshan.know_base.ai.service;

import com.roshan.know_base.ai.dto.ConversationResponse;
import com.roshan.know_base.ai.dto.MessageResponse;
import com.roshan.know_base.ai.entity.MessageRole;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ConversationService {

    Page<ConversationResponse> getUserConversations(int page, int size);

    ConversationResponse createConversation(UUID userId, String initialQuestion);

    Page<MessageResponse> getConversationMessages(UUID conversationId, int page, int size);

    void addMessageToConversation(UUID conversationId, MessageRole role, String content, List<UUID> sourceChunks);
}
