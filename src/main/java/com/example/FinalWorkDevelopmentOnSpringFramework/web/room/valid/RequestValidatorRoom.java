package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.valid;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RequestValidatorRoom {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final  DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void validate(CreateRoomRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateNotBlank(request.name(), "Название номера");
        validateNotBlank(request.description(), "Описание");
        validatePositive(request.number(), "Номер комнаты");
        validatePositive(request.price(), "Цена");
        validatePositive(request.maximumPeople(), "Максимальное количество людей");
        validatePositive(request.hotelId(), "Идентификатор отеля");
        parseDate(request.dateBegin(), "Дата начала недоступности");
        parseDate(request.dateEnd(), "Дата окончания недоступности");
    }

    private static void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private static void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " должен быть положительным числом");
        }
    }

    private static void parseDate(String dateStr, String fieldName) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустой");
        }
        try {
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат " + fieldName + ". Ожидается: yyyy-MM-dd");
        }
    }

    public static void validate(FilterRoom filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Объект фильтрации не может быть null");
        }

        String checkInStr = filter.dateCheckIn();
        String checkOutStr = filter.dateCheckOut();

        // Если обе даты отсутствуют — валидация пройдена
        if ((checkInStr == null || checkInStr.isBlank()) &&
                (checkOutStr == null || checkOutStr.isBlank())) {
            return;
        }

        LocalDate checkIn = null;
        LocalDate checkOut = null;

        if (checkInStr != null && !checkInStr.isBlank()) {
            try {
                checkIn = LocalDate.parse(checkInStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Неверный формат даты заезда (dateCheck_in). Ожидается: ГГГГ-ММ-ДД");
            }
        }

        if (checkOutStr != null && !checkOutStr.isBlank()) {
            try {
                checkOut = LocalDate.parse(checkOutStr, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Неверный формат даты выезда (dateCheck_out). Ожидается: ГГГГ-ММ-ДД");
            }
        }

        if (checkIn == null && checkOut != null) {
            throw new IllegalArgumentException("Дата заезда (dateCheck_in) обязательна, если указана дата выезда (dateCheck_out)");
        }

        // Если указаны обе даты — проверяем логику
        if (checkIn != null && checkOut != null) {
            if (!checkOut.isAfter(checkIn)) {
                throw new IllegalArgumentException("Дата выезда (dateCheck_out) должна быть строго позже даты заезда (dateCheck_in)");
            }

            LocalDate today = LocalDate.now();
            if (checkIn.isBefore(today)) {
                throw new IllegalArgumentException("Дата заезда (dateCheck_in) не может быть в прошлом");
            }
        }
    }
}

