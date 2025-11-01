package com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.valid.RequestValidator;

public record UserRequest(
        String name,
        String password,
        String eMail
) {
    public void validate() {
        new RequestValidator().validate(this);
    }
}