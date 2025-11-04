package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.valid;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.CreateBookingRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RequestValidatorBooking {

    public void validate(CreateBookingRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateNotBlank(request.dateCheckIn(), "дату заезда в отель");
        validateNotBlank(request.dateCheckOut(), "дату выезда из отеля");

        LocalDate checkIn = parseDate(request.dateCheckIn(), "даты заезда");
        LocalDate checkOut = parseDate(request.dateCheckOut(), "даты выезда");

        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }


        if (roomIdIsInvalid(request.roomId())) {
            throw new IllegalArgumentException("Идентификатор номера должен быть положительным числом");
        }
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " необходимо указать");
        }
    }

    private LocalDate parseDate(String dateStr, String fieldName) {
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат " + fieldName + ". Ожидается формат: yyyy-MM-dd");
        }
    }



    private boolean roomIdIsInvalid(Long roomId) {
        return roomId <= 0;
    }
}

