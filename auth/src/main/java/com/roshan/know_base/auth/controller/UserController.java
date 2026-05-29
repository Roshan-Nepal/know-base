package com.roshan.know_base.auth.controller;

import com.roshan.know_base.auth.dto.UserResponse;
import com.roshan.know_base.auth.dto.UserUpdateRequest;
import com.roshan.know_base.auth.service.UserService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.dto.PageResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Api for managing users.")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get a list of users.")
    public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAll(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int size){
        return ApiResponseHelper.pageResponse(userService.getAll(pageNumber, size), "User fetched successfully.");

    }
    @GetMapping("/{id}")
    @Operation(summary = "Get a single user.")
    public ResponseEntity<ApiResponse<UserResponse>> get(@PathVariable UUID id){
        return ApiResponseHelper.successResponse(userService.get(id), "OK");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch user object.")
    public ResponseEntity<ApiResponse<UserResponse>> patch(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest userPatchRequest){
        return ApiResponseHelper.successResponse(userService.patch(id, userPatchRequest), "OK");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Patch user object.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id){
        userService.delete(id);
        return ApiResponseHelper.successResponse( "User deleted successfully.");
    }
}
