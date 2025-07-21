package com.example.audiva.controller;

import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.PremiumRequest;
import com.example.audiva.service.PremiumService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/premiums")
public class PremiumController {

    PremiumService premiumService;

    @PostMapping("/buy")
    public ApiResponse<?> buyPremium(@RequestParam UUID userId, @RequestParam String premiumName, HttpServletRequest request) {
        return ApiResponse.builder()
                .result(premiumService.buyPremium(userId, premiumName, request))
                .build();
    }

    @GetMapping("/payment-return")
    public void handleVnpReturn(
            @RequestParam Optional<String> vnp_Amount,
            @RequestParam Optional<String> vnp_ResponseCode,
            @RequestParam Optional<String> vnp_TxnRef,
            HttpServletResponse response) throws IOException {

        if (vnp_ResponseCode.isPresent() && vnp_TxnRef.isPresent()) {
            premiumService.updatePaymentStatus(vnp_ResponseCode.get(), vnp_TxnRef.get());
        }

        response.sendRedirect("http://localhost:5173/thanks");
    }

    @PostMapping
    public ApiResponse<?> createPremium(@RequestBody PremiumRequest request) {
        return ApiResponse.builder()
                .result(premiumService.createPremium(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updatePremium(@PathVariable Long id, @RequestBody PremiumRequest request) {
        return ApiResponse.builder()
                .result(premiumService.updatePremium(id, request))
                .build();
    }
}
