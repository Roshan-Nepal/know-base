package com.roshan.know_base.document.service;

import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.NotFoundException;
import com.roshan.know_base.document.entity.Document;
import com.roshan.know_base.document.entity.DocumentChunk;
import com.roshan.know_base.document.entity.Tag;
import com.roshan.know_base.document.repo.DocumentChunkRepo;
import com.roshan.know_base.document.repo.DocumentContentRepo;
import com.roshan.know_base.document.repo.DocumentRepo;
import com.roshan.know_base.vector.dto.ChunkEmbeddingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentChunkingServiceImpl implements DocumentChunkingService {
    private final ChunkingService chunkingService;
    private final DocumentRepo documentRepo;
    private final DocumentContentRepo documentContentRepo;
    private final DocumentChunkRepo documentChunkRepo;

    @Override
    @Transactional
    public List<ChunkEmbeddingRequest> createChunks(UUID documentId) {
        log.info("Starting chunking of document : {} ", documentId);
        Document document = documentRepo.findById(documentId).orElseThrow(
                () -> new NotFoundException("Document not found for processing", ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND)
        );
        List<String> tags = document.getTags().stream()
                .map(Tag::getName)
                .toList();
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("documentId", documentId.toString());
        if(!tags.isEmpty()){
            metaData.put("tags", tags);
        }
        List<org.springframework.ai.document.Document> aiChunks =  chunkingService.chunkText(
                documentContentRepo.findByDocument(document).getContent(),
                metaData
        );

        List<DocumentChunk> listToBeSaved = new ArrayList<>();
        for(int i = 0; i < aiChunks.size(); i++){
            DocumentChunk documentChunk = DocumentChunk.builder()
                    .document(document)
                    .chunkIndex(i)
                    .content(aiChunks.get(i).getText())
                    .build();
            listToBeSaved.add(documentChunk);

        }
        List<ChunkEmbeddingRequest> savedChunk = documentChunkRepo.saveAll(listToBeSaved)
                .stream()
                .map(dc -> new ChunkEmbeddingRequest(dc.getId(), dc.getContent()))
                .toList();
        log.info("Saved {} chunks. Handing off to Embedding ...", savedChunk.size());
        return savedChunk;
    }
}
