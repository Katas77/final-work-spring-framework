package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Room;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingResponse {

    private Long id;
    private LocalDate dateCheck_in;
    private LocalDate dateCheck_out;
    private String user;
    private Room room;
}
