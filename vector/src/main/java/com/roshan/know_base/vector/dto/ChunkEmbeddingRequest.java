package com.roshan.know_base.vector.dto;

import java.util.UUID;

public record ChunkEmbeddingRequest (
        UUID chunkId,
        String content
){}
