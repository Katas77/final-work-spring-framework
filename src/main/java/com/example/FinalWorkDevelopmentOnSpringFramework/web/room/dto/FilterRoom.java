package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto;



import com.fasterxml.jackson.annotation.JsonProperty;

public record FilterRoom(
        String description,
        Long minPrice,
        Long maxPrice,
        Long maximumPeople,
        @JsonProperty("dateCheck_in")
        String dateCheckIn,
        @JsonProperty("dateCheck_out")
        String dateCheckOut,
        Long roomId
) {
}
