package com.roshan.know_base.ai.service;

import com.roshan.know_base.ai.dto.ConversationResponse;
import com.roshan.know_base.ai.dto.MessageResponse;
import com.roshan.know_base.ai.entity.Conversation;
import com.roshan.know_base.ai.entity.Message;
import com.roshan.know_base.ai.entity.MessageRole;
import com.roshan.know_base.ai.mapper.ConversationMapper;
import com.roshan.know_base.ai.mapper.MessageMapper;
import com.roshan.know_base.ai.repo.ConversationRepo;
import com.roshan.know_base.ai.repo.MessageRepo;
import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.NotFoundException;
import com.roshan.know_base.common.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService{

    private final ConversationRepo conversationRepository;
    private final MessageRepo messageRepository;
    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final CurrentUserProvider currentUserProvider;

    @Transactional(readOnly = true)
    public Page<ConversationResponse> getUserConversations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        UUID userId = currentUserProvider.getCurrentUserId();
        return conversationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(conversationMapper ::toResponse);
    }

    @Transactional
    public ConversationResponse createConversation(UUID userId, String initialQuestion) {
//        UUID userId = currentUserProvider.getCurrentUserId();
        // Generate a quick title based on the first question (truncate if too long)
        String title = initialQuestion.length() > 50
                ? initialQuestion.substring(0, 47) + "..."
                : initialQuestion;

        Conversation conversation = Conversation.builder()
                .userId(userId)
                .title(title)
                .build();

        Conversation saved = conversationRepository.save(conversation);
        return new ConversationResponse(saved.getId(), saved.getTitle(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public Page<MessageResponse> getConversationMessages(UUID conversationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Security check: Ensure the user actually owns this conversation
        UUID userId = currentUserProvider.getCurrentUserId();
        conversationRepository.findByIdAndUserId(conversationId, userId)
                .orElseThrow(() -> new NotFoundException("Conversation not found or access denied", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));

        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId, pageable)
                .map(messageMapper :: toResponse);
    }

    /**
     * Helper method for the RAG Service to easily save messages to the database.
     */
    @Transactional
    public void addMessageToConversation(UUID conversationId, MessageRole role, String content, List<UUID> sourceChunks) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND));

        Message message = Message.builder()
                .conversation(conversation)
                .role(role)
                .content(content)
                .sourceChunks(sourceChunks)
                .build();

        messageRepository.save(message);
    }
}
