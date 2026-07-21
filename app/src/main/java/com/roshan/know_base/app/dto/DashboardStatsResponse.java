package com.roshan.know_base.app.dto;

public record DashboardStatsResponse(
        long totalDocuments,
        long totalConversations
) {
}
