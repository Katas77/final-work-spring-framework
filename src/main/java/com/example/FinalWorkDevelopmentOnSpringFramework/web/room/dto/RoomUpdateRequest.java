package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RoomUpdateRequest(
        Long id,
        String name,
        String description,
        Long number,
        Long price,
        Long maximumPeople,
        @JsonProperty("dateBegin")
        String dateBegin,
        @JsonProperty("dateEnd")
        String dateEnd,
        Long hotelId
) {
}