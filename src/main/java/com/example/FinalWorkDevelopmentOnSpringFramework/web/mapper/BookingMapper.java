package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.CreateBookingRequest;
import java.io.UTFDataFormatException;
import java.util.List;
import java.util.stream.Collectors;

public interface BookingMapper {


    Booking createBookingToBooking(CreateBookingRequest request) throws UTFDataFormatException, BusinessLogicException;

    Booking bookingUpdateRequestToBooking(BookingUpdateRequest request) throws UTFDataFormatException, BusinessLogicException;

    BookingResponse BookingToResponse(Booking booking);

    default BookingListResponse BookingListResponseList(List<Booking> bookingList) {
        BookingListResponse response = new BookingListResponse();
        response.setBookingResponses(bookingList.stream().map(this::BookingToResponse).collect(Collectors.toList()));
        return response;
    }

}
