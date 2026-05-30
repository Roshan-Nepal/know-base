package com.roshan.know_base.auth.controller;

import com.roshan.know_base.auth.dto.*;
import com.roshan.know_base.auth.service.AuthService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Api for auth.")
public class AuthController {
    private final AuthService authService;

    @PostMapping( "/register")
    @Operation(summary = "Create a user.")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request){
        return ApiResponseHelper.successResponse(authService.register(request), "User Created Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login endpoint.")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest){
        TokenResponse response = authService.login(loginRequest);
        HttpHeaders headers = buildRefreshTokenHeader(response.refreshToken(), 7 * 24 * 60 * 60);
        return ApiResponseHelper.successResponse(response.accessToken(), "Login successfully.", headers);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password for a logged in user.")
    public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        authService.changePassword(authentication, changePasswordRequest);
        return ApiResponseHelper.successResponse( "Password changed successfully.");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user",
            description = "Clears the refresh token HttpOnly cookie and logs the user out.")
    public ResponseEntity<ApiResponse<Void>> logout() {

        HttpHeaders headers = buildRefreshTokenHeader("", 0);
        return ApiResponseHelper.successResponse(null, "Logged out successfully.", headers);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token",
            description = "Generates a new access token using the HttpOnly refresh token cookie.")
    public ResponseEntity<ApiResponse<String>> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        String newAccessToken = authService.refreshToken(refreshToken);
        return ApiResponseHelper.successResponse(newAccessToken, "Token refreshed successfully.");
    }

    private HttpHeaders buildRefreshTokenHeader(String token, long maxAge) {

        ResponseCookie cookie = ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(false) // true in production
                .path("/api/v1/auth/refresh")
                .sameSite("Strict")
                .maxAge(maxAge)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return headers;
    }
}
