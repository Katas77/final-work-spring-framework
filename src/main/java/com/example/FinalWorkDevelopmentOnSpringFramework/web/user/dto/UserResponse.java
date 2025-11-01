package com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String eMail,
        List<String> roles
) {
}