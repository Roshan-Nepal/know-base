package com.roshan.know_base.document.repo;

import com.roshan.know_base.document.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentChunkRepo extends JpaRepository<DocumentChunk, UUID> {
}
