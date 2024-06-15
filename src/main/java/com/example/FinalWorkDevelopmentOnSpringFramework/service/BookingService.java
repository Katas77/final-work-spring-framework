package com.example.FinalWorkDevelopmentOnSpringFramework.service;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface BookingService {
    List<Booking> findAll(int pageNumber, int pageSize);
    ResponseEntity<String> save(Booking booking);
    ResponseEntity<String> update(Booking booking);
    ResponseEntity<String> deleteById(Long id);
    ResponseEntity<BookingResponse> findById(Long id);

}
