package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;

import java.time.LocalDate;

public record BookingResponse(
        Long id,
        LocalDate dateCheckIn,
        LocalDate dateCheckOut,
        String userName,
        Long userId,
        Room room
) {
}