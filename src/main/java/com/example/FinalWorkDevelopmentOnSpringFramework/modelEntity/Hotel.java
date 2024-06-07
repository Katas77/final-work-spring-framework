
package com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "advertisements")
    private String headingAdvertisements;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "distance")
    private Long distance;//distance from the center  city

    @Column(name = "ratings")
    private Long ratings;

    @Column(name = "number_of_Ratings")
    private Long numberRatings;//the number of ratings on the basis of which it was calculated rating.


}


