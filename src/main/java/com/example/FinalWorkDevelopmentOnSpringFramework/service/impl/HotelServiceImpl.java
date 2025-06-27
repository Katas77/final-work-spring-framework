package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.Trackable;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
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
    public HotelResponse findById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hotel with ID " + id + " not found"));
        return hotelMapper.hotelToResponse(hotel);
    }

    @Override
    public String save(Hotel hotel) {
        hotelRepository.saveAndFlush(hotel);
       return MessageFormat.format("Hotel with nickname {0} saved", hotel.getTitle());}

    @Override
    public String update(Hotel hotel) {
        Hotel existingHotel = hotelRepository.findById(hotel.getId())
                .orElseThrow(() -> new NotFoundException("Hotel with ID " + hotel.getId() + " not found"));
        copyNonNullProperties(hotel, existingHotel);
        hotelRepository.save(existingHotel);
    return MessageFormat.format("Hotel with ID {0} updated", hotel.getId());}

    @Override
    public String deleteById(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw new NotFoundException("Hotel with ID " + id + " deleted");
        }
        hotelRepository.deleteById(id);
    return MessageFormat.format("Hotel with ID {0} i", id);}

    @Override
    public String changesRating(RatingChanges request) {
        Hotel hotel = hotelRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Hotel with ID " + request.getId() + " not found"));
        long totalRating = hotel.getRatings() * hotel.getNumberRatings();
        totalRating = totalRating - hotel.getRatings() + request.getNewMark();
        Long rating = (long) Math.ceil((double) totalRating / hotel.getNumberRatings());
        hotel.setRatings(rating);
        hotel.setNumberRatings(hotel.getNumberRatings() + 1);
        hotelRepository.save(hotel);
    return MessageFormat.format("Hotel rating with name {0} updated", hotel.getTitle());}

    @Override
    public HotelListResponse filtrate(int pageNumber, int pageSize, FilterHotel filter) {
        List<Hotel> hotelList = hotelRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                .filter(hotel -> matchesFilter(hotel, filter))
                .collect(Collectors.toList());

        if (hotelList.isEmpty()) {
            log.info("No hotels with these parameters were found");
        }
        return hotelMapper.hotelListResponseList(hotelList);
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
