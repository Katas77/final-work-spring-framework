package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.valid.RequestValidatorRoom;


import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateRoomRequest(
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
    public void validate() {
        RequestValidatorRoom.validate(this);
    }
}