package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.RoleRequest;
import com.roshan.know_base.auth.dto.RoleResponse;
import com.roshan.know_base.auth.entity.Role;
import com.roshan.know_base.auth.repo.RoleRepo;
import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.AlreadyExistException;
import com.roshan.know_base.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService{
    private final RoleRepo roleRepo;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        roleRepo.findRoleByName(roleRequest.name())
                .ifPresent(role -> {
                    throw new AlreadyExistException(
                            "Role with name '" + roleRequest.name() + "' already exists",
                            ErrorCode.ALREADY_EXISTS,
                            HttpStatus.CONFLICT
                    );
                });
        Role role = new Role();
        role.setName(roleRequest.name());
        Role savedRole = roleRepo.save(role);
        return new RoleResponse(savedRole.getId(), savedRole.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getAllRoles() {
        return roleRepo.findAll()
                .stream()
                .map(role -> new RoleResponse(role.getId(), role.getName()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse get(UUID id) {
        Role role = findRoleOrThrow(id);
        return  new RoleResponse(role.getId(), role.getName());
    }

    @Override
    public void deleteRole(UUID id) {
        Role role = findRoleOrThrow(id);
        roleRepo.delete(role);
    }

    @Override
    public RoleResponse patch(UUID id, RoleRequest roleRequest) {
        Role role = findRoleOrThrow(id);
        role.setName(roleRequest.name());
        return new RoleResponse(role.getId(), role.getName());
    }



    private Role findRoleOrThrow(UUID id) {
        return roleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "Role not found.",
                        ErrorCode.NOT_FOUND,
                        HttpStatus.NOT_FOUND
                ));
    }
}
