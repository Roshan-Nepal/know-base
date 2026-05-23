package com.roshan.know_base.auth.repo;

import com.roshan.know_base.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {
    Optional<Role> findRoleByName(String name);
}
