package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.HotelRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.utils.BeanUtils;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.RatingChanges;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.UpdateHotelRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {


    private final HotelRepository hotelRepository;


    @Override
    public List<Hotel> findAll(int pageNumber, int pageSize) {
        return hotelRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Hotel findById(Long id) {
        Optional<Hotel> byId = hotelRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            log.info(MessageFormat.format("Hotel ID {0} not found", id));
            return null;
        }
    }

    @Override
    public ResponseEntity<String> save(Hotel hotel) {
        hotelRepository.saveAndFlush(hotel);
        return ResponseEntity.ok(MessageFormat.format("Hotel with Nickname   {0} save", hotel.getTitle()));
    }

    @Override
    public ResponseEntity<String> update(Hotel hotel) {
        Optional<Hotel> existedHotel = hotelRepository.findById(hotel.getId());
        if (existedHotel.isPresent()) {
            BeanUtils.copyNonNullProperties(hotel, existedHotel.get());
            hotelRepository.save(existedHotel.get());
            return ResponseEntity.ok(MessageFormat.format("Hotel with ID {0} updated", hotel.getId()));
        } else {
            return new ResponseEntity<>(
                    MessageFormat.format("Hotel with ID {0} not found", hotel.getId()),
                    HttpStatus.NOT_FOUND);

        }
    }

    @Override
    public ResponseEntity<String> deleteById(Long id) {
        Optional<Hotel> byId = hotelRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Hotel with ID {0} not found", id));
        } else {
            hotelRepository.deleteById(id);
            return ResponseEntity.ok(MessageFormat.format("Hotel with ID {0} deleted", id));
        }
    }

    @Override
    public ResponseEntity<String> changesRating(RatingChanges request) {
        Optional<Hotel> byId = hotelRepository.findById(request.getId());
        if (byId.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Hotel with ID {0} not found", request.getId()));
        } else {
            Hotel hotel = byId.get();
            Long totalRating = hotel.getRatings() * hotel.getNumberRatings();
            totalRating = totalRating - hotel.getRatings() + request.getNewMark();
            Long rating = (long) Math.ceil(totalRating / hotel.getNumberRatings());
            hotel.setRatings(rating);
            hotel.setNumberRatings(hotel.getNumberRatings() + 1);
            hotelRepository.save(hotel);
            return ResponseEntity.ok(MessageFormat.format("Hotel rating with name {0}  updated ", hotel.getTitle()));
        }
    }

    @Override
    public List<Hotel> findFilter(int pageNumber, int pageSize, UpdateHotelRequest request) {
        return null;
    }


}
