package com.roshan.know_base.ai.service;

import com.roshan.know_base.ai.entity.MessageRole;
import com.roshan.know_base.common.security.CurrentUserProvider;
import com.roshan.know_base.document.entity.DocumentChunk;
import com.roshan.know_base.document.repo.DocumentChunkRepo;
import com.roshan.know_base.vector.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagServiceImpl implements RagService {

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepo documentChunkRepo;
    private final ConversationService conversationService;
    private final ChatClient chatClient;
    private final CurrentUserProvider userProvider;

    private static final String SYSTEM_PROMPT = """
            You are an advanced, helpful assistant connected to the user's personal knowledge base.
            Answer the user's question using ONLY the provided document context blocks.
            
            Strict Guidelines:
            1. Reply only on the clear facts mentioned directly in the context.
            2. Do not assume, extrapolate, or bring in external training data.
            3. If the context does not contain the answer, say exactly: "I cannot find sufficient information in your documents to answer this."
            
            Context:
            {context}
            """;

    @Transactional
    public SseEmitter processChat(UUID conversationId, String userQuestion) {
        SseEmitter emitter = new SseEmitter(0L); // no time out
        log.info("Processing RAG query for conversation: {}", conversationId);
        UUID userId = userProvider.getCurrentUserId();
        // Save the user's prompt to the chat history
        conversationService.addMessageToConversation(conversationId, MessageRole.USER, userQuestion, List.of());

        // RETRIEVE: Vector search to find the 5 most relevant chunk IDs
        List<UUID> matchingChunkIds = embeddingService.findSimilarChunks(userQuestion, userId, 5);

        // If no relevant chunks are found, return a fallback response.
        if (matchingChunkIds.isEmpty()) {
            String fallbackAnswer = "I cannot find sufficient information in your documents to answer this.";
            conversationService.addMessageToConversation(conversationId, MessageRole.ASSISTANT, fallbackAnswer, List.of());
            try {
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data(fallbackAnswer));
            } catch (Exception e) {
                log.error("Error while stream : {}", e.getMessage());
                emitter.completeWithError(e);
            }
            return emitter;
        }

        // FETCH TEXT: load the text for those chunk IDs
        List<DocumentChunk> relevantChunks = documentChunkRepo.findAllById(matchingChunkIds);

        // AUGMENT: Compile the context text
        String formattedContext = relevantChunks.stream()
                .map(chunk -> String.format("[Doc: %s, Chunk: %s]\n%s",
                        chunk.getDocument().getName(),
                        chunk.getId(),
                        chunk.getContent()
                ))
                .collect(Collectors.joining("\n\n--\n\n"));

        PromptTemplate promptTemplate = new PromptTemplate(SYSTEM_PROMPT);
        Prompt prompt = promptTemplate.create(Map.of("context", formattedContext));

        // GENERATE: Request generation
        log.debug("Sending compiled context prompt to LLM");

        // Holds the full streamed LLM response incrementally as tokens arrive.
        // Since the response is received in chunks (streaming), we append each token
        // to a StringBuilder inside an AtomicReference to safely accumulate state
        // across reactive callbacks.
        // Once the stream completes, the full aggregated response is persisted
        // to the conversation history along with the retrieved context chunk IDs.
        AtomicReference<StringBuilder> buffer = new AtomicReference<>(new StringBuilder());
        AtomicReference<String> modelRef = new AtomicReference<>();
        AtomicReference<Long> tokensRef = new AtomicReference<>(0L);
        //stream from LLM
        chatClient.prompt(prompt)
                .user(userQuestion)
                .stream()
                .chatResponse()
                .doOnNext(response -> {

                            String token = response.getResult()
                                    .getOutput()
                                    .getText();


                            buffer.get().append(token);

                            // model info (set once)
                            if (modelRef.get() == null) {
                                modelRef.set(response.getMetadata().getModel());
                            }

                            // token usage (if provider supports it)
                            if (response.getMetadata().getUsage() != null) {
                                tokensRef.set((long) response.getMetadata().getUsage().getTotalTokens());
                            }

                            try {
                                emitter.send(SseEmitter.event()
                                        .name("token")
                                        .data(token));
                            } catch (Exception e) {
                                emitter.completeWithError(e);
                            }
                        }
                )
                .doOnComplete(() -> {
                    log.info("LLM completed");
                    conversationService.addMessageToConversation(
                            conversationId,
                            MessageRole.ASSISTANT,
                            buffer.get().toString(),
                            matchingChunkIds);
                    try {
                        emitter.send(SseEmitter.event()
                                .name("DONE")
                                .data("[DONE]")
                        );
                        emitter.complete();
                    } catch (Exception e) {
                        log.error("Error while stream : {}", e.getMessage());
                        emitter.completeWithError(e);
                    }
                })
                .doOnError(ex -> {
                    log.error("LLM error", ex);
                    emitter.completeWithError(ex);
                })
                .subscribe();
        return emitter;
    }
}
