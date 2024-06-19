package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUpdateRequest {

    private Long id;
    private String name;
    private String description;
    private Long number;
    private Long price;
    private Long maximumPeople;
    private String dateBegin;
    private String dateEnd;
    private Long hotelId;

}
