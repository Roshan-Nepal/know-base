package com.roshan.know_base.app.controller;

import com.roshan.know_base.app.dto.DashboardStatsResponse;
import com.roshan.know_base.app.service.DashboardService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(
        name = "Dashboard",
        description = "APIs for retrieving dashboard statistics ."
)
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(
            summary = "Get dashboard statistics",
            description = "Returns summary statistics for the currently authenticated user's dashboard, such as document and conversation metrics."
    )
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getStats() {
        return ApiResponseHelper.successResponse(
                dashboardService.getStats(),
                "Dashboard stats fetched successfully."
        );
    }
}
