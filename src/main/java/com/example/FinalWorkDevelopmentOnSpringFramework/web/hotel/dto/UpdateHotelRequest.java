package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto;

public record UpdateHotelRequest(
        Long id,
        String title,
        String headingAdvertisements,
        String city,
        String address,
        Long distance
) {
}
