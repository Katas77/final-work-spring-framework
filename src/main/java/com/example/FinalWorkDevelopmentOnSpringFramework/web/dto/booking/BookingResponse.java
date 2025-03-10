package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
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
    private String userName;
    private Long userId;
    private Room room;
}
