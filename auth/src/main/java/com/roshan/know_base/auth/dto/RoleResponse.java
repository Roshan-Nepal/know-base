package com.roshan.know_base.auth.dto;

import java.util.UUID;

public record RoleResponse(
        UUID id,
        String role
) {
}
