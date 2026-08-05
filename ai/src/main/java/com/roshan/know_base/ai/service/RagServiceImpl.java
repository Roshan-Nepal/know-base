package com.roshan.know_base.ai.service;

import com.roshan.know_base.ai.entity.MessageRole;
import com.roshan.know_base.common.security.CurrentUserProvider;
import com.roshan.know_base.document.entity.DocumentChunk;
import com.roshan.know_base.document.repo.DocumentChunkRepo;
import com.roshan.know_base.vector.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private final ChatMemory chatMemory;

    private static final String SYSTEM_PROMPT = """
            You are an advanced, helpful assistant connected to the user's personal knowledge base.
            Answer the user's question using ONLY the provided document context blocks.
            
            Allowed exceptions:
            - If the user greets you, respond with a short friendly greeting.
            - If the user thanks you, respond briefly and politely.
            - If the user says goodbye, respond briefly.
            
            Strict Guidelines:
            - Answer ONLY using information explicitly present in the provided context.
            - Never answer from your own knowledge.
            - Never translate text.
            - Never explain general concepts.
            - Never answer trivia, coding, math, language, or world knowledge questions unless that information appears in the context.
            - Do not infer or make reasonable assumptions.
            
            Context:
            {context}
            """;

    private static final int TOP_K = 5;

    private static final String FALLBACK_RESPONSE =
            "I cannot find sufficient information in your documents to answer this.";

    @Transactional
    public Flux<String> processChat(UUID conversationId, String userQuestion) {

        log.info("Processing RAG query for conversation: {}", conversationId);

        var userId = userProvider.getCurrentUserId();

        // Save the user's prompt to the chat history
        conversationService.addMessageToConversation(
                conversationId,
                MessageRole.USER,
                userQuestion,
                List.of());

        // RETRIEVE: Vector search to find the 5 most relevant chunk IDs
        var matchingChunkIds =
                embeddingService.findSimilarChunks(userQuestion, userId, TOP_K);

        if (matchingChunkIds.isEmpty()) {
            conversationService.addMessageToConversation(
                    conversationId,
                    MessageRole.ASSISTANT,
                    FALLBACK_RESPONSE,
                    List.of()
            );
            return Flux.just(FALLBACK_RESPONSE);
        }
        log.debug(
                "Retrieved {} matching chunks for conversation {}",
                matchingChunkIds.size(),
                conversationId
        );

        // FETCH TEXT: load the text for those chunk IDs
        var relevantChunks =
                documentChunkRepo.findAllById(matchingChunkIds);

        // AUGMENT: Compile the context text
        var formattedContext = buildContext(relevantChunks);

        Prompt prompt = createPrompt(formattedContext);


        // GENERATE: Request generation
        return generateResponse(prompt, conversationId, matchingChunkIds, userQuestion);

    }


    private String buildContext(List<DocumentChunk> chunks){
        return chunks.stream()
                .map(chunk -> String.format(
                        "[Doc: %s, Chunk: %s]\n%s",
                        chunk.getDocument().getName(),
                        chunk.getId(),
                        chunk.getContent()))
                .collect(Collectors.joining("\n\n--\n\n"));
    }

    private Prompt createPrompt(String context){
        return new PromptTemplate(SYSTEM_PROMPT)
                .create(Map.of("context", context));
    }

    private Flux<String> generateResponse(
            Prompt prompt,
            UUID conversationId,
            List<UUID> matchingChunkIds,
            String userQuestion) {
        StringBuilder builder = new StringBuilder();
        return chatClient.prompt(prompt)
                .advisors(a ->
                    a.advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                            .param(ChatMemory.CONVERSATION_ID, conversationId.toString())
                )
                .user(userQuestion)
                .stream()
                .chatResponse()

                .map(response ->
                        response.getResult().getOutput().getText()
                )
                .doOnNext(builder::append)
                .doOnComplete(() -> {
                            conversationService.addMessageToConversation(
                                    conversationId,
                                    MessageRole.ASSISTANT,
                                    builder.toString(),
                                    matchingChunkIds);
                            log.info(
                                    "Completed RAG response for conversation {}",
                                    conversationId);
                        }
                )
                .doOnError(ex ->
                        log.error(
                                "Failed to generate response for conversation {}",
                                conversationId,
                                ex));
    }
}
