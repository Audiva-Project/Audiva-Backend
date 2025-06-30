package com.example.audiva.controller;

import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.UserCreationRequest;
import com.example.audiva.dto.request.UserUpdateRequest;
import com.example.audiva.dto.response.UserResponse;
import com.example.audiva.entity.User;
import com.example.audiva.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.createUser(request)).build();
    }

    @GetMapping
    public ApiResponse<List<User>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info(authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<User>>builder().result(userService.getAllUsers()).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> get(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder().result(userService.getUserById(userId)).build();
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> update(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder().result(userService.updateUser(userId, request)).build();
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable String userId) {
        userService.deleteUser(userId);
    }
}
