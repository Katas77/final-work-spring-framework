package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.valid;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomUpdateRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RequestValidatorRoom {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static void validate(CreateRoomRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateRequiredNotBlank(request.name(), "Название номера");
        validateRequiredNotBlank(request.description(), "Описание");
        validateRequiredPositive(request.number(), "Номер комнаты");
        validateRequiredPositive(request.price(), "Цена");
        validateRequiredPositive(request.maximumPeople(), "Максимальное количество людей");
        validateRequiredPositive(request.hotelId(), "Идентификатор отеля");

        validateRequiredDate(request.dateBegin(), "Дата начала недоступности");
        validateRequiredDate(request.dateEnd(), "Дата окончания недоступности");
    }

    public static void validate(RoomUpdateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос обновления комнаты не может быть null");
        }

        ParsedDates parsed = parseOptionalDatePair(
                request.dateBegin(),
                request.dateEnd(),
                "дата начала недоступности",
                "дата окончания недоступности"
        );

        if (parsed.date1 != null && parsed.date2 != null) {
            if (!parsed.date2.isAfter(parsed.date1)) {
                throw new IllegalArgumentException("Дата окончания недоступности должна быть строго позже даты начала недоступности");
            }
        }
    }

    public static void validate(FilterRoom filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Объект фильтрации не может быть null");
        }

        ParsedDates parsed = parseOptionalDatePair(
                filter.dateCheckIn(),
                filter.dateCheckOut(),
                "дата заезда (dateCheck_in)",
                "дата выезда (dateCheck_out)"
        );

        if (parsed.date1 != null && parsed.date2 != null) {
            if (!parsed.date2.isAfter(parsed.date1)) {
                throw new IllegalArgumentException("Дата выезда (dateCheck_out) должна быть строго позже даты заезда (dateCheck_in)");
            }

            LocalDate today = LocalDate.now();
            if (parsed.date1.isBefore(today)) {
                throw new IllegalArgumentException("Дата заезда (dateCheck_in) не может быть в прошлом");
            }
        }
    }

    private static class ParsedDates {
        final LocalDate date1;
        final LocalDate date2;

        ParsedDates(LocalDate date1, LocalDate date2) {
            this.date1 = date1;
            this.date2 = date2;
        }
    }

    private static ParsedDates parseOptionalDatePair(
            String date1Str, String date2Str,
            String date1Name, String date2Name) {

        boolean hasDate1 = date1Str != null && !date1Str.isBlank();
        boolean hasDate2 = date2Str != null && !date2Str.isBlank();

        if (!hasDate1 && !hasDate2) {
            return new ParsedDates(null, null);
        }

        LocalDate date1 = null;
        LocalDate date2 = null;

        if (hasDate1) {
            date1 = parseDate(date1Str.trim(), date1Name);
        }
        if (hasDate2) {
            date2 = parseDate(date2Str.trim(), date2Name);
        }

        // Если указана только вторая дата — ошибка
        if (date1 == null && date2 != null) {
            throw new IllegalArgumentException(
                    capitalize(date2Name) + " обязательна, если указана " + date1Name
            );
        }

        return new ParsedDates(date1, date2);
    }


    private static void validateRequiredNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private static void validateRequiredPositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(fieldName + " должен быть положительным числом");
        }
    }

    private static void validateRequiredDate(String dateStr, String fieldName) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустой");
        }
        parseDate(dateStr.trim(), fieldName);
    }


    private static LocalDate parseDate(String dateStr, String fieldName) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Некорректный формат " + fieldName + ". Ожидается: yyyy-MM-dd"
            );
        }
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}