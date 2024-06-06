
package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.DateFormatException;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.CreateBookingRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.BookingMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


import java.io.UTFDataFormatException;
import java.time.LocalDate;


@Component
@Primary
@AllArgsConstructor
public class BookingMapAllField implements BookingMapper {
    private final UserService userService;
    private final RoomService roomService;
    @Override
    public Booking createBookingToBooking(CreateBookingRequest request) throws UTFDataFormatException, DateFormatException {
        if (request == null) {
            return null;
        }
        return Booking .builder()
                .room(roomService.findById((long) request.getRoomId()))
                .user(userService.findById((long) request.getUserId()))
                .dateCheck_in(localDateOfString(request.getDateCheck_in()))
                .dateCheck_out(localDateOfString(request.getDateCheck_out()))
                .build();
    }

    @Override
    public Booking bookingUpdateRequestToBooking(BookingUpdateRequest request) throws UTFDataFormatException, DateFormatException {
        if (request == null) {
            return null;
        }
        System.out.println(request.getUserId()+"  user Id");
        System.out.println(request.getRoomId()+"  room Id");
        System.out.println(request.getBookingId()+"  booking Id");
        return Booking .builder()
                .id(request.getBookingId())
                .room(request.getRoomId()==null?null:roomService.findById(request.getRoomId()))
                .user(request.getUserId()==null?null:userService.findById(request.getUserId()))
                .dateCheck_in(request.getDateCheck_in()==null?null:localDateOfString(request.getDateCheck_in()))
                .dateCheck_out(request.getDateCheck_out()==null?null:localDateOfString(request.getDateCheck_out()))
                .build();
    }

    @Override
    public BookingResponse BookingToResponse(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingResponse.builder()
                .id(booking.getId())
                .room(booking.getRoom())
                .user(booking.getUser().getName())
                .dateCheck_in(booking.getDateCheck_in())
                .dateCheck_out(booking.getDateCheck_out())
                .build();
    }

    public LocalDate localDateOfString(String date) throws DateFormatException {
        String[] arrayDate = date.split("");
        if (arrayDate.length != 6) {
            throw new DateFormatException("Enter date in DDMMYY format. Example - 221124");
        }
        String yearSt = "20" + arrayDate[4] + arrayDate[5];
        int year = Integer.parseInt(yearSt);
        String monthSt = arrayDate[2] + arrayDate[3];
        int month = Integer.parseInt(monthSt);
        String daySt = arrayDate[0] + arrayDate[1];
        int day = Integer.parseInt(daySt);
        return LocalDate.of(year, month, day);
    }


}

