package com.example.audiva.dto.response;

import com.example.audiva.entity.Premium;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPremiumResponse {
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String premiumName;
}
