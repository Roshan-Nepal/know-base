package com.roshan.know_base.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RegisterRequest(
        @NotBlank(message = "Username cannot be blank.")
        String username,
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 8, message = "Password must be at least 8 character")
        String password,
        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Must be a valid email format.")
        String email,
        Set<String> roles
) {
}
