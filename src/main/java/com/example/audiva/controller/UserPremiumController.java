package com.example.audiva.controller;

import com.example.audiva.dto.response.UserPremiumResponse;
import com.example.audiva.service.UserPremiumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-premium")
@RequiredArgsConstructor
@Slf4j
public class UserPremiumController {
    private final UserPremiumService userPremiumService;

    @GetMapping("/me")
    public UserPremiumResponse getUserPremiumByUserId() {
        Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userPremiumService.getUserPremiumByUsername(userName);
    }
}
