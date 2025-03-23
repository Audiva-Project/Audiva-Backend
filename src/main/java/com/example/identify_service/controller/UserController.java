package com.example.identify_service.controller;

import com.example.identify_service.dto.request.ApiResponse;
import com.example.identify_service.dto.request.UserCreationRequest;
import com.example.identify_service.dto.request.UserUpdateRequest;
import com.example.identify_service.dto.response.UserReponse;
import com.example.identify_service.entity.User;
import com.example.identify_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ApiResponse<UserReponse> create(@RequestBody @Valid UserCreationRequest request) {
      return ApiResponse.<UserReponse>builder().result(userService.createUser(request)).build();
    }

    @GetMapping
    public ApiResponse<List<User>> getAll() {
        return ApiResponse.<List<User>>builder().result(userService.getAllUsers()).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserReponse> get(@PathVariable String userId) {
        return ApiResponse.<UserReponse>builder().result(userService.getUserById(userId)).build();
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
