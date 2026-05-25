package com.roshan.know_base.auth.controller;

import com.roshan.know_base.auth.dto.LoginRequest;
import com.roshan.know_base.auth.dto.RegisterRequest;
import com.roshan.know_base.auth.dto.TokenResponse;
import com.roshan.know_base.auth.dto.UserResponse;
import com.roshan.know_base.auth.service.AuthService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping( "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request){
        return ApiResponseHelper.successResponse(authService.register(request), "User Created Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest loginRequest){
        return ApiResponseHelper.successResponse(authService.login(loginRequest), "Login Success");

    }

}
