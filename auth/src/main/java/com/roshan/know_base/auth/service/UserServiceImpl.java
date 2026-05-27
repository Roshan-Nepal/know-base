package com.roshan.know_base.auth.service;

import com.roshan.know_base.auth.dto.UserResponse;
import com.roshan.know_base.auth.dto.UserUpdateRequest;
import com.roshan.know_base.auth.entity.User;
import com.roshan.know_base.auth.mapper.UserMapper;
import com.roshan.know_base.auth.repo.UserRepo;
import com.roshan.know_base.common.dto.PageResponse;
import com.roshan.know_base.common.enums.ErrorCode;
import com.roshan.know_base.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepo userRepo;
    private final UserMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAll(int pageNumber, int size) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<UserResponse> page = userRepo.findAll(pageable)
                .map(mapper::toResponse);
         return new PageResponse<>(
                 page.getContent(),
                 page.getNumber(),
                 page.getSize(),
                 page.getTotalElements(),
                 page.getTotalPages()
         );
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse get(UUID id) {
        return mapper.toResponse(findUserOrThrow(id));
    }

    @Override
    public void delete(UUID id) {
        User user = findUserOrThrow(id);
        userRepo.delete(user);
    }


    @Override
    public UserResponse patch(UUID id, UserUpdateRequest userUpdateRequest) {
        User user = findUserOrThrow(id);
        user.setEmail(userUpdateRequest.email());
        return mapper.toResponse(user);
    }

    private User findUserOrThrow(UUID id){
         return userRepo.findById(id)
                .orElseThrow(() ->  new NotFoundException(
                        "User not found",
                        ErrorCode.NOT_FOUND,
                        HttpStatus.NOT_FOUND));
    }
}
