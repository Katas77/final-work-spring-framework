package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto;


import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.valid.RequestValidatorHotel;

public record RatingChanges(
        Long id,
        Long newMark
) {
    public void validate() {
        RequestValidatorHotel.validate(this);
    }
}