package com.roshan.know_base.vector.repo;

import com.roshan.know_base.vector.entity.DocumentEmbedding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VectorRepository extends JpaRepository<DocumentEmbedding, UUID> {


    /**
     * Performs a semantic search using pgvector's cosine distance operator <=>.
     * @param queryVector the embedding vector representing the search query
     * @param userId the owner of the document embeddings to search
     * @param topK the maximum number of matching chunk IDs to return
     * @return a list of chunk IDs ranked from most to least semantically similar
     */
    @Query(nativeQuery = true, value = """
            SELECT chunk_id FROM document_embeddings
            WHERE user_id = :userId
            ORDER BY embedding <=> CAST(:queryVector AS vector)
            LIMIT :topK
            """)
    List<UUID> findTopKByCosineDistance(
            @Param("queryVector") float[] queryVector,
            @Param("userId") UUID userId,
            @Param("topK") int topK
    );
}
