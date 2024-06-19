package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterRoom {
    private String description;
    private Long minPrice;
    private Long maxPrice;
    private Long maximumPeople;
    private String dateCheck_in;
    private String dateCheck_out;
    private Long roomId;

}
