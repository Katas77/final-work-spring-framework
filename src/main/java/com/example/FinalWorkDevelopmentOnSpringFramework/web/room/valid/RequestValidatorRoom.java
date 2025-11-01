package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.valid;


import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.CreateRoomRequest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class RequestValidatorRoom {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public void validate(CreateRoomRequest request) {
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

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " должен быть положительным числом");
        }
    }

    private LocalDate parseDate(String dateStr, String fieldName) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустой");
        }
        try {
            return LocalDate.parse(dateStr.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Некорректный формат " + fieldName + ". Ожидается: yyyy-MM-dd");
        }
    }
}