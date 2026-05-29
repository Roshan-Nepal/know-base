package com.roshan.know_base.common.helper;


import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ApiResponseHelper {
    private ApiResponseHelper(){

    }
    public static <T> ResponseEntity<ApiResponse<T>> successResponse(T data, String message){
        ApiResponse<T> response = new ApiResponse<>(data,message, true, LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ApiResponse<Void>>  successResponse(String message){
        ApiResponse<Void> response = new ApiResponse<>(null,message, true, LocalDateTime.now());
        return ResponseEntity.ok(response);
    }
    public static <T> ResponseEntity<ApiResponse<T>> successResponse(T data, String message, HttpStatus status){
        ApiResponse<T> response = new ApiResponse<>(data,message, true, LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }
    public static <T> ResponseEntity<ApiResponse<T>> successResponse(
            T data,
            String message,
            HttpHeaders headers
    ) {
        ApiResponse<T> response = new ApiResponse<>(
                data,
                message,
                true,
                LocalDateTime.now()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    public static <T> ResponseEntity<ApiResponse<PageResponse<T>>> pageResponse(
            Page<T> page,
            String message
    ) {
        PageResponse<T> pageResponse = new PageResponse<>(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious()
        );

        ApiResponse<PageResponse<T>> response = new ApiResponse<>(
                pageResponse,
                message,
                true,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

}
