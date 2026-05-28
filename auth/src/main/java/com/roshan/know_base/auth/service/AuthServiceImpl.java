package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.*;
import com.roshan.know_base.auth.entity.Role;
import com.roshan.know_base.auth.entity.User;
import com.roshan.know_base.auth.mapper.UserMapper;
import com.roshan.know_base.auth.repo.RoleRepo;
import com.roshan.know_base.auth.repo.UserRepo;
import com.roshan.know_base.common.constant.SecurityConstants;
import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.BadRequestException;
import com.roshan.know_base.common.exception.NotFoundException;
import com.roshan.know_base.common.security.JwtUtil;
import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = request.roles().stream()
                .map(roleName -> roleRepo.findRoleByName(roleName)
                        .orElseThrow(() ->
                                new BadRequestException("Invalid name provided: " + roleName,
                                        ErrorCode.INVALID_ROLE,
                                        HttpStatus.BAD_REQUEST))
                )
                .collect(Collectors.toSet());
        user.setRoles(roles);
        User savedUser = userRepo.save(user);
        log.info("User created with id: {}", savedUser.getId());
        return userMapper.toResponse(savedUser);

    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {


        User user = userRepo.findUserByEmail(loginRequest.email())
                .orElseThrow(() ->
                        new BadRequestException("Invalid email or password",
                                ErrorCode.INVALID_CREDENTIAL,
                                HttpStatus.UNAUTHORIZED
                        )
                );
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new BadRequestException(
                    "Invalid email or password",
                    ErrorCode.INVALID_CREDENTIAL,
                    HttpStatus.UNAUTHORIZED
            );
        }
        String accessToken = jwtUtil.generateAccessToken(
                loginRequest.email(),
                buildClaims(user));
        String refreshToken = jwtUtil.generateRefreshToken(loginRequest.email());
        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void changePassword(Authentication authentication, ChangePasswordRequest changePasswordRequest) {
        String name = authentication.getName();
        User user = userRepo.findUserByUsername(name)
                .orElseThrow(() -> new NotFoundException(
                        "User not found.",
                        ErrorCode.NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));
        if (!passwordEncoder.matches(changePasswordRequest.oldPassword(), user.getPassword())) {
            throw new BadRequestException(
                    "Current password is incorrect",
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }
        if (passwordEncoder.matches(changePasswordRequest.newPassword(), user.getPassword())) {
            throw new BadRequestException(
                    "New password must be different from current password",
                    ErrorCode.INVALID_REQUEST,
                    HttpStatus.BAD_REQUEST
            );
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));


    }

    @Override
    public String refreshToken(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new JwtException("Invalid or expired refresh token.");
        }
        String email = jwtUtil.extractEmail(refreshToken);

        User user = userRepo.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return jwtUtil.generateAccessToken(email, buildClaims(user));
    }

    private Map<String, Object> buildClaims(User user) {
        return Map.of(
                SecurityConstants.CLAIM_ROLES,
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList(),

                SecurityConstants.CLAIM_USER_ID,
                user.getId()
        );
    }
}
