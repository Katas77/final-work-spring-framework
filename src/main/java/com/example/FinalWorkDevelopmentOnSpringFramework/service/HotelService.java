package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.FilterHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.RatingChanges;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HotelService {
    List<Hotel> findAll(int pageNumber, int pageSize);

    Hotel findById(Long id);

    String save(Hotel hotel);

    String update(Hotel hotel);

    String deleteById(Long id);

    String changesRating(RatingChanges request);

    Page<HotelResponse> filtrate(int pageNumber, int pageSize, FilterHotelRequest filter);
}
