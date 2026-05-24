package com.roshan.know_base.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
