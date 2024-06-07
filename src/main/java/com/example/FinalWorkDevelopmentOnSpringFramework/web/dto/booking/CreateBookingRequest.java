package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {


    @NotBlank(message = "дату заезда в отель    необходимо указать")
    private String dateCheck_in;
    @NotBlank(message = "даты выезда из отеля нреобходимо  указать")
    private String dateCheck_out;
    private int userId;
    private int roomId;


}
