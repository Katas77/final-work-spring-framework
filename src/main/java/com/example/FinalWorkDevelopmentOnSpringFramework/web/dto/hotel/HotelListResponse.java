package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelListResponse {
    private List<HotelResponse> hotelResponses = new ArrayList<>();
}
