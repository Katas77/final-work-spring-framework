package com.example.FinalWorkDevelopmentOnSpringFramework.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "heading_advertisements")
    private String headingAdvertisements;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "distance")
    private Long distance;

    @Column(name = "ratings")
    private Long ratings;

    @Column(name = "number_ratings")
    private Long numberRatings;
}