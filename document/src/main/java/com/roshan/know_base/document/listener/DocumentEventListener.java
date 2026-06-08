package com.roshan.know_base.document.listener;

import com.roshan.know_base.document.event.DocumentCreatedEvent;
import com.roshan.know_base.document.service.DocumentProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class DocumentEventListener {
    private final DocumentProcessingService documentProcessingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDocumentEvent(DocumentCreatedEvent documentCreatedEvent){
        log.info("Event received for document: {}", documentCreatedEvent.documentId());
        documentProcessingService.extractText(documentCreatedEvent.documentId());
    }
}
