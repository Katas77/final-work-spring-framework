package com.example.FinalWorkDevelopmentOnSpringFramework.web.user.valid;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserRequest;

public class RequestValidator {

    public void validate(UserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Запрос не может быть null");
        }

        validateNotBlank(request.name(), "Имя");
        validateNotBlank(request.password(), "Пароль");
        validateNotBlank(request.eMail(), "Email");

        validateLength(request.name(), "Имя", 1, 100);
        validateLength(request.password(), "Пароль", 6, 255);
        validateLength(request.eMail(), "Email", 5, 254);

        if (!isValidEmail(request.eMail())) {
            throw new IllegalArgumentException("Email должен быть корректным");
        }
    }

    private void validateNotBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
    }

    private void validateLength(String value, String fieldName, int min, int max) {
        if (value.length() < min) {
            throw new IllegalArgumentException(fieldName + " должно содержать минимум " + min + " символов");
        }
        if (value.length() > max) {
            throw new IllegalArgumentException(fieldName + " не должно превышать " + max + " символов");
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        int at = email.indexOf('@');
        if (at <= 0) return false;
        int dot = email.lastIndexOf('.');
        return dot > at + 1 && dot < email.length() - 1;
    }
}