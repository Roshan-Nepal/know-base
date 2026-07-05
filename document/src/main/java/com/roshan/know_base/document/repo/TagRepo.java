package com.roshan.know_base.document.repo;

import com.roshan.know_base.document.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TagRepo extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByNameAndUserId(String name, UUID userId);
}
