package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.valid.RequestValidatorBooking;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.SchemaValidator;

public record CreateBookingRequest(
        String dateCheckIn,
        String dateCheckOut,
        Long roomId
) implements SchemaValidator {
    public void validate() {
        RequestValidatorBooking.validate(this);
    }
}
