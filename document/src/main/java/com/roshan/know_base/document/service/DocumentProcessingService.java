package com.roshan.know_base.document.service;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.document.entity.Document;
import com.roshan.know_base.document.entity.DocumentContent;
import com.roshan.know_base.document.entity.DocumentStatus;
import com.roshan.know_base.document.entity.DocumentType;
import com.roshan.know_base.document.event.TextExtractedEvent;
import com.roshan.know_base.document.exception.DocumentProcessingException;
import com.roshan.know_base.document.repo.DocumentContentRepo;
import com.roshan.know_base.document.repo.DocumentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentProcessingService {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DocumentContentRepo documentContentRepo;
    private final DocumentRepo repo;
    private final StorageService storageService;
    private final ParserService parserService;
    @Async
    @Transactional
    public void extractText(UUID id) {
        log.info("Starting async parsing for document ID: {}", id);

        log.info("id: {}", id);
        Document document = repo.findById(id)
                .orElseThrow(() -> new DocumentProcessingException("Document not found for async processing",
                        ErrorCode.NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));

        DocumentContent documentContent = new DocumentContent();
        try(InputStream inputStream = storageService.loadAsResource(document.getStorage())){
            String extractedText;
            if(document.getType() == DocumentType.MARKDOWN || document.getType() == DocumentType.CODE){
                extractedText = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
            else  {
                extractedText = parserService.extractText(inputStream);
            }
            documentContent.setContent(extractedText);
            document.setStatus(DocumentStatus.READY);
        } catch (IOException e) {
            log.error("Failed to parse document ID: {}", id, e);
            document.setStatus(DocumentStatus.FAILED);
        }
        Document savedDocument = repo.saveAndFlush(document);
        documentContent.setDocument(savedDocument);

        documentContentRepo.save(documentContent);
        log.info("Async finished processing");
        applicationEventPublisher.publishEvent(new TextExtractedEvent(savedDocument.getId(), savedDocument.getUserId()));
    }
}
