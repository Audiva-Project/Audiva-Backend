package com.example.audiva.service;

import com.example.audiva.dto.request.UserCreationRequest;
import com.example.audiva.dto.request.UserUpdateRequest;
import com.example.audiva.dto.response.UserResponse;
import com.example.audiva.entity.Playlist;
import com.example.audiva.entity.User;
import com.example.audiva.enums.Role;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.UserMapper;
import com.example.audiva.repository.PlayListRepository;
import com.example.audiva.repository.RoleRepository;
import com.example.audiva.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    PasswordEncoder passwordEncoder;
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PlayListRepository playListRepository;

    //    @PreAuthorize("hasRole('ADMIN')")
    //    @PreAuthorize("hasAuthority('CREATE_DATA')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        ));
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext().getAuthentication();
        String name = context.getName();
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setIsPremium(false);
//        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        createDefaultPlaylists(savedUser);
        System.out.println("Saved user:" +savedUser);

        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    private void createDefaultPlaylists(User user) {
        boolean playlistExists = playListRepository.existsByUserIdAndName(user.getId(), "Playlist");
        String playlistImage = "1751428697154_playlist.jpg";
        if (!playlistExists) {
            Playlist playlist = new Playlist();
            playlist.setName("Playlist");
            playlist.setThumbnailUrl(playlistImage);
            playlist.setUser(user);
            playListRepository.save(playlist);
        }

        boolean favoriteListExists = playListRepository.existsByUserIdAndName(user.getId(), "Favoritelist");
        String favoriteImage = "1751429360800_favoritelist.jpg";
        if (!favoriteListExists) {
            Playlist favoriteList = new Playlist();
            favoriteList.setName("Favoritelist");
            favoriteList.setThumbnailUrl(favoriteImage);
            favoriteList.setUser(user);
            playListRepository.save(favoriteList);
        }
    }
}
