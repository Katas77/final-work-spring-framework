package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    private String title;
    private String headingAdvertisements;
    private String city;
    private String address;
    private Long distance;
    private Long ratings ;
    private Long numberRatings;

}
