package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.FilterHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.RatingChanges;

import java.util.List;

public interface HotelService {
    List<Hotel> findAll(int pageNumber, int pageSize);

    HotelResponse findById(Long id);

    String save(Hotel hotel);

    String update(Hotel hotel);

    String deleteById(Long id);

    String changesRating(RatingChanges request);

  List <HotelResponse> filtrate(int pageNumber, int pageSize, FilterHotelRequest filter);
}
