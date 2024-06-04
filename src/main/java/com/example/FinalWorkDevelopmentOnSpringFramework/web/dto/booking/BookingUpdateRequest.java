package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateRequest {


    private String dateCheck_in;
    private String dateCheck_out;
    private Long userId;
    private Long roomId;

}
