package com.roshan.know_base.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8)
        String oldPassword,
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, message = "Password must be at least 8 character")
        String newPassword
) {
}
