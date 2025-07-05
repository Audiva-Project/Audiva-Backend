package com.example.audiva.mapper;

import com.example.audiva.dto.request.PremiumRequest;
import com.example.audiva.dto.response.PremiumResponse;
import com.example.audiva.entity.Premium;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PremiumMapper {

    Premium toPremium(PremiumRequest request);

    PremiumResponse toPremiumResponse(Premium premium);

    void updatePremiumFromRequest(PremiumRequest request, @MappingTarget Premium premium);
}
