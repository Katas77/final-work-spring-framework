package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.FilterHotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.RatingChanges;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface HotelService {
    List<Hotel> findAll(int pageNumber, int pageSize);

    HotelResponse findById(Long id);

    String save(Hotel hotel);

   String update(Hotel hotel);

    String deleteById(Long id);

  String changesRating(RatingChanges request);

    HotelListResponse filtrate(int pageNumber, int pageSize, FilterHotel filter);
}
