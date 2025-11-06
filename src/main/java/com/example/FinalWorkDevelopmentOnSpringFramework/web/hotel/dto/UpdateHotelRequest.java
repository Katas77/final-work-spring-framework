package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.SchemaValidator;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.valid.RequestValidatorHotel;

public record UpdateHotelRequest(
        Long id,
        String title,
        String headingAdvertisements,
        String city,
        String address,
        Long distance
)implements SchemaValidator {
    public void validate() {
        RequestValidatorHotel.validate(this);
    }
}