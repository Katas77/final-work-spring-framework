package com.example.FinalWorkDevelopmentOnSpringFramework.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "number")
    private Long number;

    @Column(name = "price")
    private Long price;

    @Column(name = "maximum_people")
    private Long maximumPeople;

    @Column(name = "unavailable_begin")
    private LocalDate unavailableBegin;

    @Column(name = "unavailable_end")
    private LocalDate unavailableEnd;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude
    private Hotel hotel;
}