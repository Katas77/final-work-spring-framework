package com.example.FinalWorkDevelopmentOnSpringFramework.service;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Hotel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface HotelService {

    List<Hotel> findAll(int pageNumber, int pageSize);

    Hotel findById(Long id);

    ResponseEntity<String> save(Hotel hotel);

    ResponseEntity<String> update(Hotel hotel);

    ResponseEntity<String> deleteById(Long id);


}
