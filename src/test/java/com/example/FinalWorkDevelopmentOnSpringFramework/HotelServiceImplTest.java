package com.example.FinalWorkDevelopmentOnSpringFramework;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.HotelRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.impl.HotelServiceImpl;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.FilterHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.RatingChanges;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;


    @Test
    void findAll_shouldReturnHotels() {
        Page<Hotel> page = new PageImpl<>(List.of(new Hotel()));
        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Hotel> result = hotelService.findAll(0, 10);
        assertThat(result).hasSize(1);
        verify(hotelRepository).findAll(PageRequest.of(0, 10));
    }


    @Test
    void findById_existing_shouldReturnHotel() {
        Hotel hotel = new Hotel();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Hotel result = hotelService.findById(1L);
        assertThat(result).isSameAs(hotel);
    }

    @Test
    void findById_notFound_shouldThrowNotFoundException() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> hotelService.findById(1L))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void save_shouldSaveAndReturnMessage() {
        Hotel input = Hotel.builder().title("Test").build();
        Hotel saved = Hotel.builder().id(1L).title("Test").build();
        when(hotelRepository.save(input)).thenReturn(saved);

        String result = hotelService.save(input);
        assertThat(result).isEqualTo("Hotel with title Test saved");
        verify(hotelRepository).save(input);
    }


    @Test
    void update_shouldUpdateAndReturnMessage() {
        Hotel input = Hotel.builder().id(1L).title("New").build();
        Hotel existing = Hotel.builder().id(1L).title("Old").build();
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(hotelRepository.save(existing)).thenReturn(existing);

        String result = hotelService.update(input);
        assertThat(result).isEqualTo("Hotel with ID 1 updated");
        assertThat(existing.getTitle()).isEqualTo("New");
    }


    @Test
    void deleteById_existing_shouldDelete() {
        when(hotelRepository.existsById(1L)).thenReturn(true);

        String result = hotelService.deleteById(1L);
        assertThat(result).isEqualTo("Hotel with ID 1 deleted");
        verify(hotelRepository).deleteById(1L);
    }


    @Test
    void changesRating_shouldUpdateAverage() {
        Hotel hotel = Hotel.builder()
                .id(1L)
                .title("Test")
                .ratings(4L)
                .numberRatings(2L)
                .build(); // сумма = 8

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any())).thenReturn(hotel);

        RatingChanges req = new RatingChanges(1L, 5L);
        String result = hotelService.changesRating(req);

        assertThat(result).contains("Test");
        assertThat(hotel.getRatings()).isEqualTo(4); // (8 + 5) / 3 = 4.33 → 4
        assertThat(hotel.getNumberRatings()).isEqualTo(3L);
    }


    @Test
    void filtrate_withFilters_shouldReturnPage() {
        FilterHotelRequest req = new FilterHotelRequest("Test", null, null, null, null, null, null);
        List<Hotel> hotels = List.of(Hotel.builder().title("Test Hotel").build());
        when(hotelRepository.filterHotels(any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(hotels);

        Page<HotelResponse> result = hotelService.filtrate(0, 10, req);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }
}
