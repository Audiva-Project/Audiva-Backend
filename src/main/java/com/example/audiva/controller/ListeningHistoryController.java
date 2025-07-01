package com.example.audiva.controller;

import com.example.audiva.dto.request.ListeningHistoryRequest;
import com.example.audiva.service.ListeningHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/history")
public class ListeningHistoryController {
    ListeningHistoryService listeningHistoryService;

    public ListeningHistoryController(ListeningHistoryService listeningHistoryService) {
        this.listeningHistoryService = listeningHistoryService;
    }


    @PostMapping
    public ResponseEntity<?> saveHistory(@RequestBody ListeningHistoryRequest req, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
            String userId = jwtAuth.getToken().getClaim("userId");
            listeningHistoryService.save(userId, req.getSongId(), null);
        } else {
            listeningHistoryService.save(null,req.getSongId(), req.getAnonymousId());
        }
        return ResponseEntity.ok().build();
}}
