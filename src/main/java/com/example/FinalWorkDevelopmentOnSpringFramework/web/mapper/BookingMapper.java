package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.DateFormatException;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.CreateBookingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import java.io.UTFDataFormatException;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(target = "id", ignore = true)
    Booking createBookingToBooking(CreateBookingRequest request) throws UTFDataFormatException, DateFormatException;

    @Mapping(target = "id", ignore = true)
    Booking bookingUpdateRequestToBooking(BookingUpdateRequest request) throws UTFDataFormatException, DateFormatException;

    BookingResponse BookingToResponse(Booking booking);

    default BookingListResponse BookingListResponseList(List<Booking> bookingList) {
        BookingListResponse response = new BookingListResponse();
        response.setBookingResponses(bookingList.stream().map(this::BookingToResponse).collect(Collectors.toList()));
        return response;
    }

}
