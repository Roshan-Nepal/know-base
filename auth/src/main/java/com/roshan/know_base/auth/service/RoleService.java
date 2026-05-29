package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.RoleRequest;
import com.roshan.know_base.auth.dto.RoleResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    List<RoleResponse> getAllRoles();
    void deleteRole(UUID id);

    RoleResponse patch(UUID id, RoleRequest roleRequest);

    RoleResponse get(UUID id);
}
