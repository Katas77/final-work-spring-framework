package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingResponse;
import java.util.List;

public interface BookingService {
    List<Booking> findAll(int pageNumber, int pageSize);

    String save(Booking booking);

    String update(Booking booking);

    String deleteById(Long id);

    Booking findById(Long id);

}
