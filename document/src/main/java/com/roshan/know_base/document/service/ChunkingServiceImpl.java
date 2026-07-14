package com.roshan.know_base.document.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChunkingServiceImpl implements ChunkingService {
    private final TokenTextSplitter splitter = new TokenTextSplitter(
            512,                          // chunkSize: Target size of each chunk (in tokens, not characters)
            50,                           // minChunkSizeChars: Overlap/minimum size to ensure continuity
            50,                           // minChunkLengthToEmbed: Discard tiny fragments (like a single period)
            10000,                        // maxNumChunks: Hard limit to prevent memory exhaustion on massive files
            true,                         // keepSeparator: Keep the punctuation attached to the text
            List.of('.', '!', '?', '\n')  // punctuationMarks: Safe boundaries to split text without breaking sentences
    );
    @Override
    public List<Document> chunkText(String rawText, Map<String, Object> metadata) {

        if(rawText == null || rawText.trim().isEmpty()){
            return List.of();
        }

        // 2. Wrap your raw string into a Spring AI Document object.
        // By passing the metadata here, Spring AI will automatically attach
        // this metadata to EVERY smaller chunk it generates.
        Document aiDocument = new Document(rawText, metadata);
        // 3. Apply the splitter pipeline. It takes a list of documents and returns a flattened, chunked list.
        return  splitter.apply(List.of(aiDocument));
    }
}
