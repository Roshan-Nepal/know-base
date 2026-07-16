package com.roshan.know_base.document.service;

import com.roshan.know_base.vector.dto.ChunkEmbeddingRequest;

import java.util.List;
import java.util.UUID;

public interface DocumentChunkingService {

    List<ChunkEmbeddingRequest> createChunks(UUID documentId);
}
