package com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.SchemaValidator;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.valid.RequestValidator;

public record UserRequest(
        String name,
        String password,
        String eMail
) implements SchemaValidator {
    public void validate() {
        RequestValidator.validate(this);
    }
}