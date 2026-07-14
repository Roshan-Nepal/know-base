package com.roshan.know_base.vector.service;


import com.roshan.know_base.vector.dto.ChunkEmbeddingRequest;

import java.util.List;
import java.util.UUID;

public interface EmbeddingService {
    void embedAndStoreChunks(List<ChunkEmbeddingRequest> chunksToEmbed, UUID userId);
    List<UUID> findSimilarChunks(String question, UUID userId, int topK);
}
