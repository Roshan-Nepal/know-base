package com.roshan.know_base.ai.controller;

import com.roshan.know_base.ai.dto.ChatRequest;
import com.roshan.know_base.ai.dto.ConversationResponse;
import com.roshan.know_base.ai.service.ConversationService;
import com.roshan.know_base.ai.service.RagService;
import com.roshan.know_base.common.security.CurrentUserProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(
        name = "Chat",
        description = "Chat with the assistant using Server-Sent Events (SSE)."
)
public class ChatController {

    private final RagService ragService;
    private final ConversationService conversationService;
    private final CurrentUserProvider userProvider;

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendMessage(@RequestBody ChatRequest request){
        UUID userId = userProvider.getCurrentUserId();
        UUID conversationId = request.conversationId();
        String userMessage = request.message();

        // If no conversationId is supplied, initialize a new chat container seamlessly
        if(conversationId == null){
            ConversationResponse newConvo = conversationService.createConversation(userId, userMessage);
            conversationId = newConvo.id();
        }

        return ragService.processChat(conversationId, userMessage);
    }
}
