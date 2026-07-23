package com.roshan.know_base.ai.controller;

import com.roshan.know_base.ai.dto.ConversationResponse;
import com.roshan.know_base.ai.dto.MessageResponse;
import com.roshan.know_base.ai.service.ConversationService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.dto.PageResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/conversation")
@RequiredArgsConstructor
@Tag(
        name = "Conversations",
        description = "APIs for retrieving user conversations and their messages."
)
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping
    @Operation(
            summary = "List user conversations",
            description = "Returns a paginated list of conversations belonging to the currently authenticated user."
    )
    public ResponseEntity<ApiResponse<PageResponse<ConversationResponse>>> getMyConversation(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ApiResponseHelper.pageResponse(conversationService.getUserConversations(page, size), "Conversation fetched successfully.");
    }


    @Operation(
            summary = "Get conversation messages",
            description = "Returns a paginated list of messages for a specific conversation owned by the authenticated user."
    )
    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<ApiResponse<PageResponse<MessageResponse>>> getMessages(
            @PathVariable UUID conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        return ApiResponseHelper.pageResponse(conversationService.getConversationMessages(conversationId, page, size), "Message fetched successfully");
    }
}
