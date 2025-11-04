package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto;

public record BookingUpdateRequest(
        Long bookingId,
        String dateCheckIn,
        String dateCheckOut,
        Long userId,
        Long roomId
) {
}