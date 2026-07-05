package com.roshan.know_base.document.listener;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.document.event.DocumentCreatedEvent;
import com.roshan.know_base.document.event.TextExtractedEvent;
import com.roshan.know_base.document.exception.DocumentProcessingException;
import com.roshan.know_base.document.service.DocumentChunkingService;
import com.roshan.know_base.document.service.DocumentProcessingService;
import com.roshan.know_base.vector.dto.ChunkEmbeddingRequest;
import com.roshan.know_base.vector.service.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class DocumentEventListener {
    private final DocumentProcessingService documentProcessingService;
    private final DocumentChunkingService chunkingService;
    private final EmbeddingService embeddingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDocumentCreated(DocumentCreatedEvent event){
        log.info("Phase 1: Starting extraction for document: {}", event.documentId());        //extract text from the document
        documentProcessingService.extractText(event.documentId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTextExtracted(TextExtractedEvent event){
        log.info("Phase 2: Starting chunking and embedding for document: {}", event.documentId());

        try {
            //chunk the texts
            List<ChunkEmbeddingRequest> chunks = chunkingService.createChunks(event.documentId());
            // embed the texts
            embeddingService.embedAndStoreChunks(chunks);
        } catch (Exception e){
            log.error("Failed to process chunks for document: {}", event.documentId(), e);
            throw new DocumentProcessingException("Failed to process chunks for document : {}" +  event.documentId(),
                    ErrorCode.DOCUMENT_PROCESSING_FAILED, HttpStatus.UNPROCESSABLE_CONTENT);
        }
    }
}
