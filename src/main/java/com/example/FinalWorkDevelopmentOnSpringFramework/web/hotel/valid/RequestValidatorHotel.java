package com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.valid;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.CreateHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.RatingChanges;

public class RequestValidatorHotel {

    public void validate(CreateHotelRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateNotBlank(request.title(), "Название отеля");
        validateNotBlank(request.headingAdvertisements(), "Заголовок рекламы");
        validateNotBlank(request.city(), "Город");
        validateNotBlank(request.address(), "Адрес");

        validatePositive(request.distance(), "Расстояние");
        validateRating(request.ratings(), "Рейтинг");
        validatePositive(request.numberRatings(), "Количество оценок");
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private void validatePositive(Long value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " не может быть null");
        }
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " не может быть отрицательным");
        }
    }

    private void validateRating(Long rating, String fieldName) {
        if (rating == null) {
            throw new IllegalArgumentException(fieldName + " не может быть null");
        }
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException(fieldName + " должен быть в диапазоне от 0 до 5");
        }
    }
    public void validate(RatingChanges request) {
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
}