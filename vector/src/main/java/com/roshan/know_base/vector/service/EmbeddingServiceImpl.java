package com.roshan.know_base.vector.service;

import com.roshan.know_base.common.security.CurrentUserProvider;
import com.roshan.know_base.vector.dto.ChunkEmbeddingRequest;
import com.roshan.know_base.vector.entity.DocumentEmbedding;
import com.roshan.know_base.vector.repo.VectorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingServiceImpl implements EmbeddingService{
    private final EmbeddingModel embeddingModel;
    private final VectorRepository vectorRepository;

    @Override
    @Async
    @Transactional
    public void embedAndStoreChunks(List<ChunkEmbeddingRequest> embeddingRequestList, UUID userId) {
        for(ChunkEmbeddingRequest chunkToEmbed : embeddingRequestList){
            UUID chunkId = chunkToEmbed.chunkId();
            String content = chunkToEmbed.content();

            // call the embedding model to get the vector
            float[] vector = embeddingModel.embed(content);

            DocumentEmbedding documentEmbedding = DocumentEmbedding.builder()
                    .chunkID(chunkId)
                    .embedding(vector)
                    .userId(userId)
                    .build();
            vectorRepository.save(documentEmbedding);
        }
        log.info("Successfully embedded and stored vector" );
    }

    @Override
    public List<UUID> findSimilarChunks(String question, UUID userId, int topK) {

        float[] queryVector = embeddingModel.embed(question);
        return vectorRepository.findTopKByCosineDistance(queryVector, userId, topK);
    }


}
