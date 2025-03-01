package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.Trackable;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.HotelRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.FilterHotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.RatingChanges;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.HotelMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<Hotel> findAll(int pageNumber, int pageSize) {
        return hotelRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public ResponseEntity<HotelResponse> findById(Long id) {
        return hotelRepository.findById(id)
                .map(hotel -> ResponseEntity.status(HttpStatus.OK).body(hotelMapper.hotelToResponse(hotel)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @Override
    public ResponseEntity<String> save(Hotel hotel) {
        hotelRepository.saveAndFlush(hotel);
        return ResponseEntity.ok(MessageFormat.format("Hotel with nickname {0} saved", hotel.getTitle()));
    }

    @Override
    public ResponseEntity<String> update(Hotel hotel) {
        return hotelRepository.findById(hotel.getId())
                .map(existingHotel -> {
                    copyNonNullProperties(hotel, existingHotel);
                    hotelRepository.save(existingHotel);
                    return ResponseEntity.ok(MessageFormat.format("Hotel with ID {0} updated", hotel.getId()));
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(MessageFormat.format("Hotel with ID {0} not found", hotel.getId())));
    }

    @Override
    public ResponseEntity<String> deleteById(Long id) {
        return hotelRepository.findById(id)
                .map(hotel -> {
                    hotelRepository.deleteById(id);
                    return ResponseEntity.ok(MessageFormat.format("Hotel with ID {0} deleted", id));
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(MessageFormat.format("Hotel with ID {0} not found", id)));
    }

    @Override
    public ResponseEntity<String> changesRating(RatingChanges request) {
        return hotelRepository.findById(request.getId())
                .map(hotel -> {
                    long totalRating = hotel.getRatings() * hotel.getNumberRatings();
                    totalRating = totalRating - hotel.getRatings() + request.getNewMark();
                    Long rating = (long) Math.ceil((double) totalRating / hotel.getNumberRatings());
                    hotel.setRatings(rating);
                    hotel.setNumberRatings(hotel.getNumberRatings() + 1);
                    hotelRepository.save(hotel);
                    return ResponseEntity.ok(MessageFormat.format("Hotel rating with name {0} updated", hotel.getTitle()));
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(MessageFormat.format("Hotel with ID {0} not found", request.getId())));
    }
    @Trackable
    @Override
    public ResponseEntity<HotelListResponse> filtrate(int pageNumber, int pageSize, FilterHotel filterHotel) {
        List<Hotel> hotelList = hotelRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                .filter(hotel -> matchesFilter(hotel, filterHotel))
                .collect(Collectors.toList());

        if (hotelList.isEmpty()) {
            log.info("No hotels with these parameters were found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(hotelMapper.hotelListResponseList(hotelList));
        }

        return ResponseEntity.ok(hotelMapper.hotelListResponseList(hotelList));
    }

    private boolean matchesFilter(Hotel hotel, FilterHotel filterHotel) {
        return (filterHotel.getCity() == null || hotel.getCity().equals(filterHotel.getCity())) &&
                (filterHotel.getDistance() == null || hotel.getDistance().equals(filterHotel.getDistance())) &&
                (filterHotel.getAddress() == null || hotel.getAddress().equals(filterHotel.getAddress())) &&
                (filterHotel.getNumberRatings() == null || hotel.getNumberRatings().equals(filterHotel.getNumberRatings())) &&
                (filterHotel.getHeadingAdvertisements() == null || hotel.getHeadingAdvertisements().equals(filterHotel.getHeadingAdvertisements())) &&
                (filterHotel.getRatings() == null || hotel.getRatings().equals(filterHotel.getRatings())) &&
                (filterHotel.getTitle() == null || hotel.getTitle().equals(filterHotel.getTitle()));
    }

    private void copyNonNullProperties(Hotel source, Hotel destination) {
        if (source.getTitle() != null) {
            destination.setTitle(source.getTitle());
        }
        if (source.getCity() != null) {
            destination.setCity(source.getCity());
        }
        if (source.getDistance() != null) {
            destination.setDistance(source.getDistance());
        }
        if (source.getAddress() != null) {
            destination.setAddress(source.getAddress());
        }
        if (source.getNumberRatings() != null) {
            destination.setNumberRatings(source.getNumberRatings());
        }
        if (source.getHeadingAdvertisements() != null) {
            destination.setHeadingAdvertisements(source.getHeadingAdvertisements());
        }
        if (source.getRatings() != null) {
            destination.setRatings(source.getRatings());
        }
    }
}
