package com.roshan.know_base.auth.controller;

import com.roshan.know_base.auth.dto.*;
import com.roshan.know_base.auth.service.AuthService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest){
        return ApiResponseHelper.successResponse(authService.login(loginRequest), "Login Success");

    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password for a logged in user.")
    public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        authService.changePassword(authentication, changePasswordRequest);
        return ApiResponseHelper.successResponse( "Password changed successfully.");
    }
}
