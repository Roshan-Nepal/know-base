package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.UserResponse;
import com.roshan.know_base.auth.dto.UserUpdateRequest;
import com.roshan.know_base.common.dto.PageResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface UserService {
    Page<UserResponse> getAll(int pageNumber, int size);
    UserResponse get(UUID id);
    void delete(UUID id);
    UserResponse patch(UUID id, UserUpdateRequest userUpdateRequest);
}
