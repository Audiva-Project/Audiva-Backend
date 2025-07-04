package com.example.audiva.controller;

import com.example.audiva.dto.request.ListeningHistoryRequest;
import com.example.audiva.dto.response.ListeningHistoryResponse;
import com.example.audiva.service.ListeningHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class ListeningHistoryController {
    @Autowired
    ListeningHistoryService listeningHistoryService;

    @PostMapping
    public ResponseEntity<?> saveHistory(@RequestBody ListeningHistoryRequest req, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) auth;
            String userId = jwtAuth.getToken().getClaim("userId");
            listeningHistoryService.save(userId, req.getSongId(), null);
        } else {
            listeningHistoryService.save(null, req.getSongId(), req.getAnonymousId());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ListeningHistoryResponse>> getHistory(
            @RequestParam(required = false) String anonymousId,
            Authentication auth) {

        if (auth != null && auth.isAuthenticated()) {
            String userId = ((JwtAuthenticationToken) auth).getToken().getClaim("userId");
            return ResponseEntity.ok(listeningHistoryService.getHistoryForUser(userId));
        } else if (anonymousId != null) {
            return ResponseEntity.ok(listeningHistoryService.getHistoryForAnonymous(anonymousId));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
