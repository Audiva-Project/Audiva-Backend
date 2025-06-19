package com.example.audiva.mapper;

import com.example.audiva.dto.request.PermissionRequest;
import com.example.audiva.dto.response.PermissionResponse;
import com.example.audiva.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
