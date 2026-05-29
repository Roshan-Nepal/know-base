package com.roshan.know_base.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Please enter a valid email.")
        String email,
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, message = "Password must be at least 8 characters.")
        String password
) {
}
