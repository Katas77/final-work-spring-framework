package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderBookingEvent {
    private Long UserId;
    private String dateCheck_in;
    private String dateCheck_out;
    private String recordingFacts;
}
