package com.example.identify_service.controller;

import com.example.identify_service.dto.request.ApiResponse;
import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserReponse;
import com.example.identify_service.entity.User;
import com.example.identify_service.service.UserService;
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
    public ApiResponse<UserReponse> create(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserReponse>builder().result(userService.createUser(request)).build();
    }

    @GetMapping
    public ApiResponse<List<User>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info(authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<User>>builder().result(userService.getAllUsers()).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserReponse> get(@PathVariable String userId) {
        return ApiResponse.<UserReponse>builder().result(userService.getUserById(userId)).build();
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserReponse> getMyInfo() {
        return ApiResponse.<UserReponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserReponse> update(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserReponse>builder().result(userService.updateUser(userId, request)).build();
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable String userId) {
        userService.deleteUser(userId);
    }
}
