package com.roshan.know_base.document.service;

import org.springframework.ai.document.Document;

import java.util.List;
import java.util.Map;

public interface ChunkingService {
    List<Document> chunkText(String rawText, Map<String, Object> metaData);
}