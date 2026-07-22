package com.roshan.know_base.document.repo;

import com.roshan.know_base.document.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentRepo extends JpaRepository<Document, UUID> {

    long countByUserId(UUID userId);

    Page<Document> findAllByUserId(UUID userId, Pageable pageable);
}
