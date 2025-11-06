package com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.valid;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.CreateBookingRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class RequestValidatorBooking {

    public static void validate(CreateBookingRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateDatesForCreate(request.dateCheckIn(), request.dateCheckOut());

        if (roomIdIsInvalid(request.roomId())) {
            throw new IllegalArgumentException("Идентификатор номера должен быть положительным числом");
        }
    }

    public static void validate(BookingUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateDatesForUpdate(request.dateCheckIn(), request.dateCheckOut());

        if (request.roomId() != null && roomIdIsInvalid(request.roomId())) {
            throw new IllegalArgumentException("Идентификатор номера должен быть положительным числом");
        }
    }

    private static void validateDatesForCreate(String checkInStr, String checkOutStr) {
        validateNotBlank(checkInStr, "дату заезда в отель");
        validateNotBlank(checkOutStr, "дату выезда из отеля");

        LocalDate checkIn = parseDateNonBlank(checkInStr, "даты заезда");
        LocalDate checkOut = parseDateNonBlank(checkOutStr, "даты выезда");

        validateCheckInNotInPast(checkIn);
        validateCheckOutAfterToday(checkOut);
        validateCheckOutAfterCheckIn(checkIn, checkOut);
    }

    private static void validateDatesForUpdate(String checkInStr, String checkOutStr) {
        // Если обе даты null — пропускаем валидацию
        if (checkInStr == null && checkOutStr == null) {
            return;
        }

        // Валидация отдельных полей на пустоту (но не на null)
        if (checkInStr != null) validateOptionalNotBlank(checkInStr, "дату заезда");
        if (checkOutStr != null) validateOptionalNotBlank(checkOutStr, "дату выезда");

        LocalDate today = LocalDate.now();
        LocalDate checkIn = null;
        LocalDate checkOut = null;

        if (checkInStr != null) {
            checkIn = parseDateNonBlank(checkInStr, "даты заезда");
        }
        if (checkOutStr != null) {
            checkOut = parseDateNonBlank(checkOutStr, "даты выезда");
        }

        // Случай: обе даты заданы
        if (checkIn != null && checkOut != null) {
            validateCheckInNotInPast(checkIn);
            validateCheckOutAfterToday(checkOut);
            validateCheckOutAfterCheckIn(checkIn, checkOut);
        }
        // Только дата заезда
        else if (checkIn != null) {
            validateCheckInNotInPast(checkIn);
        }
        // Только дата выезда
        else if (checkOut != null) {
            validateCheckOutAfterToday(checkOut);
        }
    }

    private static void validateCheckInNotInPast(LocalDate checkIn) {
        LocalDate today = LocalDate.now();
        if (checkIn.isBefore(today)) {
            throw new IllegalArgumentException("Дата заезда не может быть в прошлом");
        }
    }

    private static void validateCheckOutAfterToday(LocalDate checkOut) {
        LocalDate today = LocalDate.now();
        if (!checkOut.isAfter(today)) {
            throw new IllegalArgumentException("Дата выезда не может быть в прошлом или сегодня");
        }
    }

    private static void validateCheckOutAfterCheckIn(LocalDate checkIn, LocalDate checkOut) {
        if (!checkOut.isAfter(checkIn)) {
            throw new IllegalArgumentException("Дата выезда должна быть позже даты заезда");
        }
    }

    private static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " необходимо указать");
        }
    }

    private static void validateOptionalNotBlank(String value, String fieldName) {
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустой строкой");
        }
    }

    private static LocalDate parseDateNonBlank(String dateStr, String fieldName) {
        // Предполагается, что dateStr != null и не пустая
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат " + fieldName + ". Ожидается формат: yyyy-MM-dd");
        }
    }

    private static boolean roomIdIsInvalid(Long roomId) {
        return roomId != null && roomId <= 0;
    }
}