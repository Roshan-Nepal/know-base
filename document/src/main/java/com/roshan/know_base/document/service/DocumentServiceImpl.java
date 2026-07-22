package com.roshan.know_base.document.service;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.NotFoundException;
import com.roshan.know_base.common.security.CurrentUserProvider;
import com.roshan.know_base.document.dto.DocumentResponse;
import com.roshan.know_base.document.entity.*;
import com.roshan.know_base.document.event.DocumentCreatedEvent;
import com.roshan.know_base.document.mapper.DocumentMapper;
import com.roshan.know_base.document.repo.DocumentContentRepo;
import com.roshan.know_base.document.repo.DocumentRepo;
import com.roshan.know_base.document.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
    private final CurrentUserProvider userProvider;
    private final TagRepo tagRepo;
    @Override
    @Transactional
    public DocumentResponse uploadDocument(MultipartFile file, List<String> tags) {
        log.info("Receiving document upload: {}", file.getOriginalFilename());
        UUID currentUserId = userProvider.getCurrentUserId();
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
                .userId(currentUserId)
                .name(file.getOriginalFilename())
                .metaData(metaData)
                .status(DocumentStatus.PROCESSING)
                .build();

        if (tags != null && !tags.isEmpty()) {
            Set<Tag> tagSet = new HashSet<>();
            for (String tagName : tags) {
                String normalizedName = tagName.trim().toLowerCase();

                Tag tagObj = tagRepo.findByNameAndUserId(normalizedName, currentUserId)
                        .orElseGet(() -> tagRepo.save(new Tag(normalizedName, currentUserId)));

                tagSet.add(tagObj);
            }
            document.setTags(tagSet);
        }
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

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentResponse> getAll(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return repo.findAllByUserId(userProvider.getCurrentUserId(), pageable)
                .map(documentMapper::toResponse);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Document document = repo.findById(id).orElseThrow(() -> new NotFoundException(
                "Document not for id: "+ id, ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND
        ));
        document.softDelete(userProvider.getCurrentUsername());
        repo.delete(document);
    }


}
