package com.roshan.know_base.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Please enter a valid email.")
        String email
) {
}
