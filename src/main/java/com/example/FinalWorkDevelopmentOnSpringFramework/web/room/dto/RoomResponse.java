package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto;

import java.time.LocalDate;

public record RoomResponse(
        Long id,
        String name,
        String description,
        Long number,
        Long price,
        Long maximumPeople,
        LocalDate dateBegin,
        LocalDate dateEnd,
        String hotel
) {
}