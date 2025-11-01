package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.CreateHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.UpdateHotelRequest;

import java.util.List;
import java.util.stream.Collectors;

public class HotelMapper {

    // CreateHotelRequest → Hotel
    public static Hotel toEntity(CreateHotelRequest request) {
        request.validate();
        return Hotel.builder()
                .title(request.title())
                .headingAdvertisements(request.headingAdvertisements())
                .city(request.city())
                .address(request.address())
                .distance(request.distance())
                .ratings(request.ratings())
                .numberRatings(request.numberRatings())
                .build();
    }


    public static Hotel  updateFromRequest(UpdateHotelRequest request) {
        return Hotel.builder()
                .id(request.id())
                .title(request.title())
                .headingAdvertisements(request.headingAdvertisements())
                .city(request.city())
                .address(request.address())
                .distance(request.distance())
                .build();
    }

    // Hotel → HotelResponse
    public static HotelResponse toResponse(Hotel hotel) {
        if (hotel == null) {
            return null;
        }
        return new HotelResponse(
                hotel.getId(),
                hotel.getTitle(),
                hotel.getHeadingAdvertisements(),
                hotel.getCity(),
                hotel.getAddress(),
                hotel.getDistance(),
                hotel.getRatings(),
                hotel.getNumberRatings()
        );
    }


    public static List<HotelResponse> toResponseList(List<Hotel> hotels) {
        if (hotels == null) {
            return List.of();
        }
        return hotels.stream()
                .map(HotelMapper::toResponse)
                .collect(Collectors.toList());
    }
}