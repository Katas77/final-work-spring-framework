package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.valid;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.*;

public class RequestValidatorHotel {

    public static void validate(CreateHotelRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateRequiredNotBlank(request.title(), "Название отеля");
        validateRequiredNotBlank(request.headingAdvertisements(), "Заголовок рекламы");
        validateRequiredNotBlank(request.city(), "Город");
        validateRequiredNotBlank(request.address(), "Адрес");

        validateRequiredPositive(request.distance(), "Расстояние");
        validateRequiredRating(request.ratings(), "Рейтинг");
        validateRequiredPositive(request.numberRatings(), "Количество оценок");
    }

    public static void validate(RatingChanges request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }
        if (request.newMark() == null) {
            throw new IllegalArgumentException("Поле рейтинг должно быть заполнено!");
        }
        long mark = request.newMark();
        if (mark < 1 || mark > 5) {
            throw new IllegalArgumentException("Рейтинг не может быть меньше 1 и больше 5!");
        }
    }

    public static void validate(FilterHotelRequest request) {
        if (request == null) return; // фильтр может быть полностью пустым

        validateOptionalString(request.title(), "Название отеля");
        validateOptionalString(request.headingAdvertisements(), "Заголовок рекламы");
        validateOptionalString(request.city(), "Город");
        validateOptionalString(request.address(), "Адрес");

        validateOptionalPositive(request.distance(), "Расстояние");
        validateOptionalRating(request.ratings(), "Рейтинг");
        validateOptionalPositive(request.numberRatings(), "Количество оценок");
    }

    public static void validate(UpdateHotelRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }
        if (request.id() == null || request.id() <= 0) {
            throw new IllegalArgumentException("Идентификатор отеля (id) должен быть положительным числом");
        }

        validateOptionalString(request.title(), "Название отеля");
        validateOptionalString(request.headingAdvertisements(), "Заголовок рекламы");
        validateOptionalString(request.city(), "Город");
        validateOptionalString(request.address(), "Адрес");
        validateOptionalPositive(request.distance(), "Расстояние");
    }

    // === Валидация обязательных полей (для Create) ===

    private static void validateRequiredNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private static void validateRequiredPositive(Long value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " не может быть null");
        }
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " не может быть отрицательным");
        }
    }

    private static void validateRequiredRating(Long rating, String fieldName) {
        if (rating == null) {
            throw new IllegalArgumentException(fieldName + " не может быть null");
        }
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException(fieldName + " должен быть в диапазоне от 0 до 5");
        }
    }

    // === Валидация опциональных полей (для Filter/Update) ===

    private static void validateOptionalString(String value, String fieldName) {
        if (value != null && value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустой строкой");
        }
    }

    private static void validateOptionalPositive(Long value, String fieldName) {
        if (value != null && value < 0) {
            throw new IllegalArgumentException(fieldName + " не может быть отрицательным");
        }
    }

    private static void validateOptionalRating(Long rating, String fieldName) {
        if (rating != null && (rating < 0 || rating > 5)) {
            throw new IllegalArgumentException(fieldName + " должен быть в диапазоне от 0 до 5");
        }
    }
}