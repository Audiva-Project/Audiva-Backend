package com.example.audiva.mapper;

import com.example.audiva.dto.request.UserCreationRequest;
import com.example.audiva.dto.request.UserUpdateRequest;
import com.example.audiva.dto.response.PlaylistResponse;
import com.example.audiva.dto.response.UserResponse;
import com.example.audiva.entity.Playlist;
import com.example.audiva.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PlaylistMapper.class})
public interface UserMapper {
    User toUser(UserCreationRequest user);

    @Mapping(source = "playlists", target = "playlists")
    UserResponse toUserResponse(User user);

//    @Mapping(target = "roles", ignore = true)
//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
