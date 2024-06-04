package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Statistics")
@Builder
public class Statistics {
    @Id
    private String id;
    private  String event;
}
