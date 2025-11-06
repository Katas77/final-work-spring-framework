package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto;

import com.example.FinalWorkDevelopmentOnSpringFramework.web.SchemaValidator;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.valid.RequestValidatorRoom;
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
)implements SchemaValidator {
        public void validate() {
                RequestValidatorRoom.validate(this);
        }
}