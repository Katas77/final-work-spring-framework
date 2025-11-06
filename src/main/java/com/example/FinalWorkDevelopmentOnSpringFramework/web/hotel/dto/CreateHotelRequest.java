package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.SchemaValidator;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.valid.RequestValidatorHotel;

public record CreateHotelRequest(
        String title,
        String headingAdvertisements,
        String city,
        String address,
        Long distance,
        Long ratings,
        Long numberRatings
) implements SchemaValidator {
    public void validate() {
        RequestValidatorHotel.validate(this);
    }
}
