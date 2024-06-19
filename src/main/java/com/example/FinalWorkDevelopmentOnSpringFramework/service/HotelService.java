package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.FilterHotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.RatingChanges;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface HotelService {
    List<Hotel> findAll(int pageNumber, int pageSize);

    ResponseEntity<HotelResponse> findById(Long id);

    ResponseEntity<String> save(Hotel hotel);

    ResponseEntity<String> update(Hotel hotel);

    ResponseEntity<String> deleteById(Long id);

    ResponseEntity<String> changesRating(RatingChanges request);

    ResponseEntity<HotelListResponse> filtrate(int pageNumber, int pageSize, FilterHotel filter);
}
