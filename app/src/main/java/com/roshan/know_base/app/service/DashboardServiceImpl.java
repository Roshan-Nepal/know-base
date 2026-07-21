package com.roshan.know_base.app.service;

import com.roshan.know_base.ai.repo.ConversationRepo;
import com.roshan.know_base.app.dto.DashboardStatsResponse;
import com.roshan.know_base.common.security.CurrentUserProvider;
import com.roshan.know_base.document.repo.DocumentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DocumentRepo documentRepo;
    private final ConversationRepo conversationRepo;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        UUID userId = currentUserProvider.getCurrentUserId();

        long totalDocuments = documentRepo.countByUserId(userId);
        long totalConversations = conversationRepo.countByUserId(userId);

        return new DashboardStatsResponse(totalDocuments, totalConversations);
    }
}
