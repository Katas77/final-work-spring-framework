package com.example.FinalWorkDevelopmentOnSpringFramework.repository;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
