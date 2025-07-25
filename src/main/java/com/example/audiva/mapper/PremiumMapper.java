package com.example.audiva.mapper;

import com.example.audiva.dto.request.PremiumRequest;
import com.example.audiva.entity.Premium;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PremiumMapper {

    Premium toPremium(PremiumRequest request);
}
