package com.roshan.know_base.document.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_chunks")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class DocumentChunk  {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(name = "chunk_index")
    private Integer chunkIndex;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(name = "token_count")
    private Integer tokenCount;

    @Column(name = "start_char_pos")
    private Integer startCharPost;

    @Column(name = "end_char_pos")
    private Integer endCharPost;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;



}
