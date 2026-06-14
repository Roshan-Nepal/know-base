package com.roshan.know_base.document.service;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.NotFoundException;
import com.roshan.know_base.document.dto.DocumentResponse;
import com.roshan.know_base.document.entity.Document;
import com.roshan.know_base.document.entity.DocumentContent;
import com.roshan.know_base.document.entity.DocumentStatus;
import com.roshan.know_base.document.entity.DocumentType;
import com.roshan.know_base.document.event.DocumentCreatedEvent;
import com.roshan.know_base.document.exception.DocumentProcessingException;
import com.roshan.know_base.document.mapper.DocumentMapper;
import com.roshan.know_base.document.repo.DocumentContentRepo;
import com.roshan.know_base.document.repo.DocumentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepo repo;
    private final ParserService parserService;
    private final StorageService storageService;
    private final DocumentMapper documentMapper;
    private final ApplicationEventPublisher publisher;
    private final DocumentContentRepo contentRepo;
    @Override
    @Transactional
    public DocumentResponse uploadDocument(MultipartFile file) {
        log.info("Receiving document upload: {}", file.getOriginalFilename());

        UUID docID = UUID.randomUUID();
        String storage = storageService.store(file, docID);
        Map<String, Object> metaData = Map.of(
                "contentType", file.getContentType() != null ? file.getContentType() : "unknown"
        );
        DocumentType type = parserService.determineDocumentType(file.getContentType(), file.getOriginalFilename());
        Document document = Document.builder()
                .fileSize(file.getSize())
                .type(type)
                .storage(storage)
                .name(file.getOriginalFilename())
                .metaData(metaData)
                .status(DocumentStatus.PROCESSING)
                .build();

        Document savedDoc = repo.save(document);
        publisher.publishEvent(new DocumentCreatedEvent(savedDoc.getId()));
        return documentMapper.toResponse(savedDoc);
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentResponse get(UUID id) {
        Document document = repo.findById(id).orElseThrow(() -> new NotFoundException(
                "Document not for id: "+ id, ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND
        ));
        String content = contentRepo.findById(id)
                .map(DocumentContent::getContent)
                .orElse("");
        return documentMapper.toResponse(document, content);
    }


}
