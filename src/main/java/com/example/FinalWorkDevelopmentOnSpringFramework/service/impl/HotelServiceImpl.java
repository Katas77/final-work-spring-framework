package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.HotelRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.FilterHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.RatingChanges;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.mapper.HotelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findAll(int pageNumber, int pageSize) {
        PageParams params = normalizePageParams(pageNumber, pageSize);
        return hotelRepository.findAll(PageRequest.of(params.page(), params.size())).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
       return hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Hotel with ID {0} not found", id)));
    }

    @Override
    public String save(Hotel hotel) {
        Hotel saved = hotelRepository.save(hotel);
        log.info("Saved hotel id={} title={}", saved.getId(), saved.getTitle());
        return MessageFormat.format("Hotel with title {0} saved", saved.getTitle());
    }

    @Override
    public String update(Hotel hotel) {
        Hotel existingHotel = this.findById(hotel.getId());
        copyNonNullProperties(hotel, existingHotel);
        hotelRepository.save(existingHotel);
        log.info("Updated hotel id={}", existingHotel.getId());
        return MessageFormat.format("Hotel with ID {0} updated", hotel.getId());
    }

    @Override
    public String deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (!hotelRepository.existsById(id)) {
            throw new NotFoundException(MessageFormat.format("Hotel with ID {0} not found", id));
        }
        hotelRepository.deleteById(id);
        log.info("Deleted hotel id={}", id);
        return MessageFormat.format("Hotel with ID {0} deleted", id);
    }

    @Override
    public String changesRating(RatingChanges request) {
        if (request == null || request.id() == null) {
            throw new IllegalArgumentException("Request and request.id must not be null");
        }
        if (request.newMark() < 0) {
            throw new IllegalArgumentException("newMark must be non-negative");
        }

        Hotel hotel = hotelRepository.findById(request.id())
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Hotel with ID {0} not found", request.id())));

        long currentCount = hotel.getNumberRatings() == null ? 0L : hotel.getNumberRatings();
        long currentAvg = hotel.getRatings() == null ? 0L : hotel.getRatings();

        long currentSum = currentAvg * currentCount;
        long newSum = currentSum + request.newMark();
        long newCount = currentCount + 1;

        long newAvg = Math.round((double) newSum / newCount);

        hotel.setNumberRatings(newCount);
        hotel.setRatings(newAvg);

        hotelRepository.save(hotel);
        log.info("Updated rating for hotel id={} title={} newAvg={} newCount={}", hotel.getId(), hotel.getTitle(), newAvg, newCount);

        return MessageFormat.format("Hotel rating with title {0} updated", hotel.getTitle());
    }
    @Override
    @Transactional(readOnly = true)
    public Page<HotelResponse> filtrate(int pageNumber, int pageSize, FilterHotelRequest req) {
        int page = Math.max(0, pageNumber);
        int size = Math.max(1, pageSize);

        List<Hotel> hotels = hotelRepository.filterHotels(
                toPattern(req.title()),
                toPattern(req.headingAdvertisements()),
                toPattern(req.city()),
                toPattern(req.address()),
                req.distance(),
                req.ratings(),
                req.numberRatings()
        );

        if (hotels.isEmpty()) {
            log.info("No hotels with these parameters were found");
        }

        List<HotelResponse> responses = HotelMapper.toResponseList(hotels);
        return convertListToPageSafe(responses, PageRequest.of(page, size));
    }

    public <T> Page<T> convertListToPageSafe(List<T> list, Pageable pageable) {
        int page = Math.max(0, pageable.getPageNumber());
        int size = Math.max(1, pageable.getPageSize());
        int start = page * size;

        if (start >= list.size()) {
            return new PageImpl<>(List.of(), pageable, list.size());
        }

        int end = Math.min(start + size, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
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

    private PageParams normalizePageParams(int pageNumber, int pageSize) {
        int p = Math.max(pageNumber, 0);
        int s = pageSize <= 0 ? 10 : pageSize;
        return new PageParams(p, s);
    }

    private static class PageParams {
        private final int page;
        private final int size;

        PageParams(int page, int size) {
            this.page = page;
            this.size = size;
        }

        int page() { return page; }
        int size() { return size; }
    }
    private String toPattern(String s) {
        return (s == null || s.isBlank()) ? null : "%" + s.toLowerCase() + "%";
    }


}



