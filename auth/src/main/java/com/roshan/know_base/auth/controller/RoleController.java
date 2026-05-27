package com.roshan.know_base.auth.controller;

import com.roshan.know_base.auth.dto.RoleRequest;
import com.roshan.know_base.auth.dto.RoleResponse;
import com.roshan.know_base.auth.service.RoleService;
import com.roshan.know_base.common.dto.ApiResponse;
import com.roshan.know_base.common.helper.ApiResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Role Controller", description = "Api for managing roles.")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @Operation(summary = "Create a role")
    public ResponseEntity<ApiResponse<RoleResponse>> create(@Valid @RequestBody RoleRequest roleRequest){
        return ApiResponseHelper.successResponse(roleService.createRole(roleRequest),
                "Role created Successfully.",
                HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all roles.")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll(){
        return ApiResponseHelper.successResponse(roleService.getAllRoles(), "OK");
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get a single role.")
    public ResponseEntity<ApiResponse<RoleResponse>> get(@PathVariable UUID id){
        return ApiResponseHelper.successResponse(roleService.get(id), "OK");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role object.")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id){
        roleService.deleteRole(id);
        return ApiResponseHelper.successResponse("Role deleted successfully.");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Patch a role object.")
    public ResponseEntity<ApiResponse<RoleResponse>> patch(@PathVariable UUID id, @Valid @RequestBody RoleRequest roleRequest){
        return  ApiResponseHelper.successResponse(roleService.patch(id, roleRequest), "Role patched successfully.");
    }
}
