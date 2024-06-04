package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHotelRequest {
    @NotBlank(message = "название отеля должно быть указано")
    private String title;

    @NotBlank(message = "заголовок объявления необходимо указать")
    private String  headingAdvertisements;

    @NotBlank(message = " город, в котором расположен отель,объявления необходимо указать")
    private String  city;

    @NotBlank(message = " адрес  отеля необходимо указать")
    private String  address;

    @NotBlank(message = " расстояние от центра города необходимо указать")
    private Long distance;

    @NotBlank(message = "поле рейтинг     должно быть заполнено!")
    @Size(min = 1, max = 5, message = " рейтинг   не может быть меньше {min} и больше {max}!")
    private Long ratings ;

    @NotBlank(message = "поле  количество оценок   должно быть заполнено!")
    private Long numberRatings;


}
