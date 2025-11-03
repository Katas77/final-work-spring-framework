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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public HotelResponse findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Hotel with ID {0} not found", id)));
        return HotelMapper.toResponse(hotel);
    }

    @Override
    public String save(Hotel hotel) {
        Hotel saved = hotelRepository.save(hotel);
        log.info("Saved hotel id={} title={}", saved.getId(), saved.getTitle());
        return MessageFormat.format("Hotel with title {0} saved", saved.getTitle());
    }

    @Override
    public String update(Hotel hotel) {
        if (hotel == null || hotel.getId() == null) {
            throw new IllegalArgumentException("Hotel and its id must not be null");
        }
        Hotel existingHotel = hotelRepository.findById(hotel.getId())
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Hotel with ID {0} not found", hotel.getId())));

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
    public List<HotelResponse> filtrate(int pageNumber, int pageSize, FilterHotelRequest filter) {
        PageParams params = normalizePageParams(pageNumber, pageSize);

        List<Hotel> hotelList = hotelRepository.findAll(PageRequest.of(params.page(), params.size())).getContent().stream()
                .filter(hotel -> matchesFilter(hotel, filter))
                .collect(Collectors.toList());

        if (hotelList.isEmpty()) {
            log.info("No hotels with these parameters were found");
        }
        return HotelMapper.toResponseList(hotelList);
    }

    private boolean matchesFilter(Hotel hotel, FilterHotelRequest filterHotel) {
        if (filterHotel == null) return true;

        return (filterHotel.city() == null || Objects.equals(hotel.getCity(), filterHotel.city()))
                && (filterHotel.distance() == null || Objects.equals(hotel.getDistance(), filterHotel.distance()))
                && (filterHotel.address() == null || Objects.equals(hotel.getAddress(), filterHotel.address()))
                && (filterHotel.numberRatings() == null || Objects.equals(hotel.getNumberRatings(), filterHotel.numberRatings()))
                && (filterHotel.headingAdvertisements() == null || Objects.equals(hotel.getHeadingAdvertisements(), filterHotel.headingAdvertisements()))
                && (filterHotel.ratings() == null || Objects.equals(hotel.getRatings(), filterHotel.ratings()))
                && (filterHotel.title() == null || Objects.equals(hotel.getTitle(), filterHotel.title()));
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
        int p = pageNumber < 0 ? 0 : pageNumber;
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
}



