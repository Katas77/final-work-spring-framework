package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingEvent {
    private Long UserId;
    private String dateCheck_in;
    private String dateCheck_out;
    private String recordingFacts;
}
