package com.roshan.know_base.auth.mapper;

import com.roshan.know_base.auth.dto.RegisterRequest;
import com.roshan.know_base.auth.dto.UserResponse;
import com.roshan.know_base.auth.entity.Role;
import com.roshan.know_base.auth.entity.User;
import com.roshan.know_base.common.config.CentralMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = CentralMapperConfig.class)
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleToString")
    UserResponse toResponse(User user);

    @Named("mapRoleToString")
    default String roleToString(Role role){
        if(role == null){
            return null;
        }
        return role.getName();
    }

}
