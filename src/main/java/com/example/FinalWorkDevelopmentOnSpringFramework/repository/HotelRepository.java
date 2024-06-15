package com.example.FinalWorkDevelopmentOnSpringFramework.repository;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Hotel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
