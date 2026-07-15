package com.roshan.know_base.auth.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        LocalDateTime createAt,
        Set<String> roles
) {
}
