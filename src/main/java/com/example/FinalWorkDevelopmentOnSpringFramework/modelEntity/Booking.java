package com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dateCheck_in")
    private LocalDate dateCheck_in;

    @Column(name = "dateCheck_out")
    private LocalDate dateCheck_out;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_room")
    @ToString.Exclude
    private Room room;

}
