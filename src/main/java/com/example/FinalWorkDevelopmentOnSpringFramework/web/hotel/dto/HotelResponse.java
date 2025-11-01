package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto;

public record HotelResponse(
        Long id,
        String title,
        String headingAdvertisements,
        String city,
        String address,
        Long distance,
        Long ratings,
        Long numberRatings
) {
}