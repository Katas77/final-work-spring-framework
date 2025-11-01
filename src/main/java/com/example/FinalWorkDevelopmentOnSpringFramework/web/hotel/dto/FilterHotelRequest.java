package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto;

public record FilterHotelRequest(
        String title,
        String headingAdvertisements,
        String city,
        String address,
        Long distance,
        Long ratings,
        Long numberRatings
) {

}