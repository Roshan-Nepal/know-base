package com.roshan.know_base.ai.repo;

import com.roshan.know_base.ai.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepo extends JpaRepository<Message, UUID> {

    Page<Message> findByConversationIdOrderByCreatedAtAsc(UUID conversationId, Pageable pageable);
}
