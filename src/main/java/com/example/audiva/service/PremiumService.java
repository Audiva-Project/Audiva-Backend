package com.example.audiva.service;

import com.example.audiva.dto.request.PremiumRequest;
import com.example.audiva.entity.Premium;
import com.example.audiva.entity.User;
import com.example.audiva.entity.UserPremium;
import com.example.audiva.enums.PaymentStatus;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.PremiumMapper;
import com.example.audiva.repository.PremiumRepository;
import com.example.audiva.repository.UserPremiumRepository;
import com.example.audiva.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PremiumService {

    UserRepository userRepository;
    PremiumRepository premiumRepository;
    UserPremiumRepository userPremiumRepository;
    VNPayService vnPayService;
    PremiumMapper premiumMapper;

    public String buyPremium(UUID userId, String premiumName, HttpServletRequest request) {
        User user = userRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));

        Premium premium = premiumRepository.findByName(premiumName)
                .orElseThrow(() -> new AppException(ErrorCode.PREMIUM_NOT_EXISTED));

        UserPremium existingSubscription = userPremiumRepository.findByUserIdAndPremiumName(user.getId(), premium.getName())
                .orElse(null);

        if (existingSubscription != null && existingSubscription.getStatus() == PaymentStatus.SUCCESS) {
            throw new AppException(ErrorCode.PREMIUM_ALREADY_EXISTED);
        }

        // Tạo mã giao dịch và IP
        String paymentRef = UUID.randomUUID().toString().replace("-", "");
        String ip = vnPayService.getIpAddress(request);

        // Tạo URL thanh toán
        String vnpUrl;
        try {
            vnpUrl = vnPayService.generateVNPayURL(premium.getPrice(), paymentRef, ip);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Failed to generate VNPay URL", e);
        }

        UserPremium userPremium = userPremiumRepository.findByUserId(user.getId())
                .orElse(UserPremium.builder()
                        .user(user)
                        .premium(premium)
                        .build());

        // Ghi đè các thông tin mới
        userPremium.setPaymentRef(paymentRef);
        userPremium.setStatus(PaymentStatus.PENDING);
        userPremium.setStartDate(null);
        userPremium.setEndDate(null);

        userPremiumRepository.save(userPremium);

        return vnpUrl;
    }

    public void updatePaymentStatus(String vnp_ResponseCode, String paymentRef) {
        UserPremium userPremium = userPremiumRepository.findByPaymentRef(paymentRef)
                .orElseThrow(() -> new AppException(ErrorCode.PREMIUM_NOT_EXISTED));

        if ("00".equals(vnp_ResponseCode)) {
            userPremium.setStatus(PaymentStatus.SUCCESS);
            userPremium.setStartDate(LocalDateTime.now());
            userPremium.setEndDate(LocalDateTime.now().plusDays(userPremium.getPremium().getDuration()));
        } else {
            userPremium.setStatus(PaymentStatus.FAILED);
        }

        userPremiumRepository.save(userPremium);
    }

    public Premium createPremium(PremiumRequest request) {
        if (request.getPrice() <= 0) {
            throw new AppException(ErrorCode.INVALID_PREMIUM_PRICE);
        }

        if (premiumRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PREMIUM_EXISTED);
        }

        Premium premium = premiumMapper.toPremium(request);

        SecurityContext context = SecurityContextHolder.getContext();

        if (context != null && context.getAuthentication() != null) {
            String name = context.getAuthentication().getName();
            premium.setCreatedBy(name);
        }

        return premiumRepository.save(premium);
    }

    public Premium updatePremium(Long id, PremiumRequest request) {
        Premium existedPremium = premiumRepository.existsById(id)
                ? premiumRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PREMIUM_NOT_EXISTED))
                : null;

        if (request.getPrice() <= 0) {
            throw new AppException(ErrorCode.INVALID_PREMIUM_PRICE);
        }

        premiumMapper.updatePremiumFromRequest(request, existedPremium);

        SecurityContext context = SecurityContextHolder.getContext();

        if (context != null && context.getAuthentication() != null) {
            String name = context.getAuthentication().getName();
            existedPremium.setModifiedBy(name);
        }

        return premiumRepository.save(existedPremium);
    }


    public boolean hasActivePremium(User user) {
        return user.getPremiumSubscriptions().stream()
                .anyMatch(p -> p.getStatus() == PaymentStatus.SUCCESS
                        && p.getStartDate() != null
                        && p.getEndDate() != null
                        && !LocalDateTime.now().isBefore(p.getStartDate())
                        && !LocalDateTime.now().isAfter(p.getEndDate()));
    }
}
