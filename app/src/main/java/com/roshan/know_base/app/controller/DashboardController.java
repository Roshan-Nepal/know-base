package com.roshan.know_base.app.controller;

import com.roshan.know_base.app.dto.DashboardStatsResponse;
import com.roshan.know_base.app.service.DashboardService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats() {
        return ApiResponseHelper.successResponse(
                dashboardService.getStats(),
                "Dashboard stats fetched successfully."
        );
    }
}
