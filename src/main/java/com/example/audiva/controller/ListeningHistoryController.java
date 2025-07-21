package com.example.audiva.controller;

import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.ListeningHistoryRequest;
import com.example.audiva.dto.response.ListeningHistoryResponse;
import com.example.audiva.service.ListeningHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/histories")
public class ListeningHistoryController {
    @Autowired
    ListeningHistoryService listeningHistoryService;

    @PostMapping
    public ResponseEntity<?> saveHistory(@RequestBody ListeningHistoryRequest req, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            String userName = auth.getName();
            listeningHistoryService.save(userName, req.getSongId(), null);
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
            String userName = auth.getName();
            return ResponseEntity.ok(listeningHistoryService.getHistoryForUser(userName));
        } else if (anonymousId != null) {
            return ResponseEntity.ok(listeningHistoryService.getHistoryForAnonymous(anonymousId));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // merge request
    @PostMapping("/merge")
    public ApiResponse<Void> mergeAnonymousHistory(
            @RequestBody ListeningHistoryRequest request
    ) {
        listeningHistoryService.mergeAnonymousHistory(request.getAnonymousId());

        return ApiResponse.<Void>builder()
                .result(null)
                .message("Merge anonymous history success")
                .build();
    }

}
