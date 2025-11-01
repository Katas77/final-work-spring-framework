package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.valid.RequestValidatorBooking;

public record CreateBookingRequest(
        String dateCheckIn,
        String dateCheckOut,
        int userId,
        int roomId
) {
    public void validate() {
        new RequestValidatorBooking().validate(this);
    }
}
