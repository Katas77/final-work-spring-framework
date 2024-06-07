package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomResponse {


    private Long id;
    private String name;
    private String description;
    private Long number;
    private Long price;
    private Long maximumPeople;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private Hotel hotel;
}
