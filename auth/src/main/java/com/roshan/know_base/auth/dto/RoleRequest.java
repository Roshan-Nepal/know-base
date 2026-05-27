package com.roshan.know_base.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleRequest(
        @NotBlank(message = "Role cannot be blank.")
        String name
) {
}
