package com.example.identify_service.mapper;

import com.example.identify_service.dto.request.PermissionRequest;
import com.example.identify_service.dto.response.PermissionResponse;
import com.example.identify_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
