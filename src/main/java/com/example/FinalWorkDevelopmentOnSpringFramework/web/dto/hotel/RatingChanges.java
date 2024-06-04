package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingChanges {
    private Long id;
    private Long newMark;
}
