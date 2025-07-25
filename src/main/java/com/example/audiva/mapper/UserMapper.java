package com.example.audiva.mapper;

import com.example.audiva.dto.request.UserCreationRequest;
import com.example.audiva.dto.response.UserResponse;
import com.example.audiva.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlaylistMapper.class})
public interface UserMapper {
    User toUser(UserCreationRequest user);

    @Mapping(source = "playlists", target = "playlists")
    UserResponse toUserResponse(User user);
}
