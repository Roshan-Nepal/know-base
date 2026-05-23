package com.roshan.know_base.common.dto;


import java.time.LocalDateTime;

public record ApiResponse<T>(
        T data,
        String message,
        boolean success,
        LocalDateTime timeStamp
        ) {
}
