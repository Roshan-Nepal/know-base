package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.*;
import org.springframework.security.core.Authentication;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest loginRequest);

    void changePassword(Authentication authentication, ChangePasswordRequest changePasswordRequest);

    String refreshToken(String refreshToken);
}
