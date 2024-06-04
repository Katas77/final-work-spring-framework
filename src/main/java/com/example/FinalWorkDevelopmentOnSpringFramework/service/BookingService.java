package com.example.FinalWorkDevelopmentOnSpringFramework.service;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Booking;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookingService {
    List<Booking> findAll(int pageNumber, int pageSize);

    Booking findById(Long id);

    ResponseEntity<String> save(Booking booking);

    ResponseEntity<String>  update(Booking booking);
    void dellAll();
    public ResponseEntity<String> deleteById(Long id);

}
