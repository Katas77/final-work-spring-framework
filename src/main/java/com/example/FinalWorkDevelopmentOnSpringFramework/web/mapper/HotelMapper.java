package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;


import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.*;

import java.util.List;
import java.util.stream.Collectors;


public interface HotelMapper {


    Hotel createHotelRequestToHotel(CreateHotelRequest createHotelRequest);

    Hotel updateHotelRequestToHotel(UpdateHotelRequest updateHotelRequest);

    Hotel filterToHotel(FilterHotel request);

    HotelResponse hotelToResponse(Hotel hotel);

    default HotelListResponse hotelListResponseList(List<Hotel> hotels) {
        HotelListResponse response = new HotelListResponse();
        response.setHotelResponses(hotels.stream().map(this::hotelToResponse).collect(Collectors.toList()));
        return response;
    }
}
