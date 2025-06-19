package com.example.audiva.mapper;

import com.example.audiva.dto.request.RoleRequest;
import com.example.audiva.dto.response.RoleResponse;
import com.example.audiva.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
