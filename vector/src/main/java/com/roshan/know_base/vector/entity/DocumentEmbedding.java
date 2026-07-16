package com.roshan.know_base.vector.entity;

import com.roshan.know_base.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "document_embeddings")
@Entity
public class DocumentEmbedding {

    @Id
    private UUID id;

    @Column(name = "chunk_id")
    private UUID chunkID;

    @Column(name = "user_id")
    private UUID userId;

    @JdbcTypeCode(SqlTypes.VECTOR)
    @Column(columnDefinition = "vector(768)")
    private float[] embedding;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

}
