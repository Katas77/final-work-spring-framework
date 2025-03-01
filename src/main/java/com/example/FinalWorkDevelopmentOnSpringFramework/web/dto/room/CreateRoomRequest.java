package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {

    @NotBlank(message = "название комнаты должно быть указано")
    private String name;
    @NotBlank(message = "описание комнаты  необходимо указать")
    private String description;
    @Size(min = 1, max = 155, message = "номер комнаты не может быть меньше {min} и больше {max}!")
    @NotBlank(message = " номер комнаты необходимо указать")
    private Long number;
    @NotBlank(message = " цену  за комнату  необходимо указать")
    @Size(min = 50, max = 1550, message = "цена не может быть меньше {min} и больше {max}!")
    private Long price;
    @NotBlank(message = " максимальное количество людей, которое можно разместить необходимо указать")
    @Size(min = 1, max = 5, message = " рейтинг   не может быть меньше {min} и больше {max}!")
    private Long maximumPeople;
    @NotBlank(message = "даты, когда комната недоступна   необходимо указать")
    private String dateBegin;
    @NotBlank(message = "даты, когда комната недоступна   необходимо указать")
    private String dateEnd;
    @NotBlank(message = "Id отеля   необходимо указать")
    private Long hotelId;

}
