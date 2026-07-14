package com.roshan.know_base.ai.controller;

import com.roshan.know_base.ai.dto.ConversationResponse;
import com.roshan.know_base.ai.dto.MessageResponse;
import com.roshan.know_base.ai.service.ConversationService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.dto.PageResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ConversationResponse>>> getMyConversation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ApiResponseHelper.pageResponse(conversationService.getUserConversations(page, size), "Conversation fetched successfully.");
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<ApiResponse<PageResponse<MessageResponse>>> getMessages(
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        return ApiResponseHelper.pageResponse(conversationService.getConversationMessages(conversationId, page, size), "Message fetched successfully");
    }
}
