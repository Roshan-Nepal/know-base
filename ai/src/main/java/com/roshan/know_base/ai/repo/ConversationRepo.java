package com.roshan.know_base.ai.repo;

import com.roshan.know_base.ai.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepo extends JpaRepository<Conversation, UUID> {

    Page<Conversation> findAllByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Optional<Conversation> findByIdAndUserId(UUID id, UUID userId);
}
