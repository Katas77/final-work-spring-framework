package com.example.FinalWorkDevelopmentOnSpringFramework.model;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
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

    @Column(name = "date_check_in")
    private LocalDate dateCheck_in;

    @Column(name = "date_check_out")
    private LocalDate dateCheck_out;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @ToString.Exclude
    private Room room;
}
