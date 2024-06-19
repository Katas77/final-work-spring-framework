package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor@Builder
public class RatingChanges {
    private Long id;
    @NotBlank(message = "поле рейтинг     должно быть заполнено!")
    @Size(min = 1, max = 5, message = " рейтинг   не может быть меньше {min} и больше {max}!")
    private Long newMark;

}
