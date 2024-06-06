
package com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity;


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
    @Column(name = "name;")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "number")
    private Long  number;
    @Column(name = "price")
    private Long  price;
    @Column(name = "maximum_number_of_people ")
    private Long   maximumPeople;// maximum number of people allowed
    @Column(name = "dateBegin")
    private LocalDate unavailableBegin;
    @Column(name = "dateEnd")
    private LocalDate unavailableEnd;
    @ManyToOne
    @JoinColumn(name = "id_hotel")
    @ToString.Exclude
    private Hotel hotel;



}



