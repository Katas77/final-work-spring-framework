package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEvent {
   private Long UserId;
   private String recordingFacts;

}
