package com.example.audiva.service;

import com.example.audiva.dto.response.UserPremiumResponse;
import com.example.audiva.entity.User;
import com.example.audiva.entity.UserPremium;
import com.example.audiva.enums.PaymentStatus;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.repository.UserPremiumRepository;
import com.example.audiva.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserPremiumService {
    UserPremiumRepository userPremiumRepository;
    UserRepository userRepository;

    public UserPremiumResponse getUserPremiumByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userPremiumRepository.findByUserId(user.getId())
                .map(premium -> UserPremiumResponse.builder()
                        .status(PaymentStatus.SUCCESS.name())
                        .startDate(premium.getStartDate())
                        .endDate(premium.getEndDate())
                        .build())
                .orElse(UserPremiumResponse.builder()
                        .status(PaymentStatus.NOT_SUBSCRIBED.name())
                        .startDate(null)
                        .endDate(null)
                        .build());
    }

    public UserPremium getUserPremiumByPaymentRef(String paymentRef) {
        return userPremiumRepository.findByPaymentRef(paymentRef)
                .orElseThrow(() -> new AppException(ErrorCode.PREMIUM_NOT_EXISTED));
    }

    public void deleteUserPremiumByPaymentRef(String paymentRef) {
        userPremiumRepository.findByPaymentRef(paymentRef)
                .ifPresent(userPremiumRepository::delete);
    }
}
