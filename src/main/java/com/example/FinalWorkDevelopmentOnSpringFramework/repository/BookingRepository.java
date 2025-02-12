package com.example.FinalWorkDevelopmentOnSpringFramework.repository;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
