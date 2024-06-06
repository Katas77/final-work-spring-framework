package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {
    HotelMapper INSTANCE = Mappers.getMapper(HotelMapper.class);
    @Mapping(target = "id", ignore = true)
    Hotel createHotelRequestToHotel(CreateHotelRequest createHotelRequest);
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "numberRatings", ignore = true)
    Hotel updateHotelRequestToHotel(UpdateHotelRequest updateHotelRequest );

    @Mapping(target = "id", ignore = true)
    Hotel filterToHotel(FilterHotel request);

    HotelResponse hotelToResponse(Hotel hotel);

    default HotelListResponse hotelListResponseList(List<Hotel> hotels) {
        HotelListResponse response = new HotelListResponse();
        response.setHotelResponses(hotels.stream().map(this::hotelToResponse).collect(Collectors.toList()));
        return response;
    }
}
