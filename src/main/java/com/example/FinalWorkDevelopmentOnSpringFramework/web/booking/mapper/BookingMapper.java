package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.mapper;
import com.example.FinalWorkDevelopmentOnSpringFramework.security.AppUserPrincipal;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.CreateBookingRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {


    public static Booking toEntity(CreateBookingRequest request, AppUserPrincipal userDetails) {
        LocalDate checkIn = parseDate(request.dateCheckIn());
        LocalDate checkOut = parseDate(request.dateCheckOut());
        User user = new User();
        user.setId( userDetails.getID());
        Room room = new Room();
        room.setId( request.roomId());

        return Booking.builder()
                .dateCheck_in(checkIn)
                .dateCheck_out(checkOut)
                .user(user)
                .room(room)
                .build();
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static BookingResponse toResponse(Booking booking) {
        if (booking == null) return null;

        String userName = (booking.getUser() != null && booking.getUser().getName() != null)
                ? booking.getUser().getName()
                : null;

        Long userId = (booking.getUser() != null) ? booking.getUser().getId() : null;

        return new BookingResponse(
                booking.getId(),
                booking.getDateCheck_in(),
                booking.getDateCheck_out(),
                userName,
                userId,
                booking.getRoom()
        );
    }


    public static List<BookingResponse> toResponseList(List<Booking> bookings) {
        if (bookings == null) return List.of();
        return bookings.stream()
                .map(BookingMapper::toResponse)
                .collect(Collectors.toList());
    }


    public static Booking updateFromRequest(BookingUpdateRequest request) {
        if (request == null) {
            return null;
        }
        LocalDate checkIn = parseDate(request.dateCheckIn());
        LocalDate checkOut = parseDate(request.dateCheckOut());
        User user = new User();
        user.setId((long) request.userId());

        Room room = new Room();
        room.setId((long) request.roomId());

        return Booking.builder()
                .id(request.bookingId())
                .dateCheck_in(checkIn)
                .dateCheck_out(checkOut)
                .user(user)
                .room(room)
                .build();
    }


    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат  Ожидается: yyyy-MM-dd");
        }
    }
}
