package com.roshan.know_base.common.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> data,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
