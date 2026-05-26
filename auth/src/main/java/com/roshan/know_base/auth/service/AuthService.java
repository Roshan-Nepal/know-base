package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.LoginRequest;
import com.roshan.know_base.auth.dto.RegisterRequest;
import com.roshan.know_base.auth.dto.TokenResponse;
import com.roshan.know_base.auth.dto.UserResponse;
import lombok.NonNull;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    TokenResponse login(LoginRequest loginRequest);

}
