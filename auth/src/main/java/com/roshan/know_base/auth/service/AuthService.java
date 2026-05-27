package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.*;
import com.roshan.know_base.auth.entity.User;
import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.security.core.Authentication;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest loginRequest);

    void changePassword(Authentication authentication, @Valid ChangePasswordRequest changePasswordRequest);
}
