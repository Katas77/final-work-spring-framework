package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.SchemaValidator;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.valid.RequestValidatorBooking;

public record BookingUpdateRequest(
        Long bookingId,
        String dateCheckIn,
        String dateCheckOut,
        Long userId,
        Long roomId
) implements SchemaValidator {
    public void validate() {
        RequestValidatorBooking.validate(this);
    }
}
