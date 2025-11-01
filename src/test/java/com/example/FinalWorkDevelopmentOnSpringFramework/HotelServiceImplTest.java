package com.example.FinalWorkDevelopmentOnSpringFramework;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.HotelRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.impl.HotelServiceImpl;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.FilterHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.RatingChanges;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel h1;
    private Hotel h2;

    @BeforeEach
    void setUp() {
        h1 = Hotel.builder()
                .id(1L)
                .title("Hotel One")
                .city("CityA")
                .distance(10L)
                .address("Addr1")
                .numberRatings(2L)
                .ratings(4L)
                .headingAdvertisements("Ad1")
                .build();

        h2 = Hotel.builder()
                .id(2L)
                .title("Hotel Two")
                .city("CityB")
                .distance(20L)
                .address("Addr2")
                .numberRatings(0L)
                .ratings(0L)
                .headingAdvertisements("Ad2")
                .build();
    }

    @Test
    void findAll_normalizesNegativePageAndSize() {
        when(hotelRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(asList(h1, h2)));

        List<Hotel> res = hotelService.findAll(-5, 0);

        assertThat(res).containsExactly(h1, h2);
        verify(hotelRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void findById_found_and_notFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(h1));
        HotelResponse resp = hotelService.findById(1L);
        assertThat(resp).isNotNull();

        when(hotelRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> hotelService.findById(99L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void save_initializesRatingsIfNull() {
        Hotel input = Hotel.builder()
                .id(3L)
                .title("New")
                .numberRatings(null)
                .ratings(null)
                .build();

        when(hotelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String res = hotelService.save(input);
        assertThat(res).contains("New");

        ArgumentCaptor<Hotel> captor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository).save(captor.capture());
        Hotel saved = captor.getValue();
        assertThat(saved.getNumberRatings()).isEqualTo(0L);
        assertThat(saved.getRatings()).isEqualTo(0L);
    }

    @Test
    void update_copiesNonNullProperties() {
        Hotel patch = Hotel.builder()
                .id(1L)
                .title("Updated Title")
                .build();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(h1));
        when(hotelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String res = hotelService.update(patch);
        assertThat(res).contains("updated");

        ArgumentCaptor<Hotel> captor = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository).save(captor.capture());
        Hotel saved = captor.getValue();
        assertThat(saved.getTitle()).isEqualTo("Updated Title");
        assertThat(saved.getCity()).isEqualTo(h1.getCity());
    }

    @Test
    void deleteById_notFound_throws() {
        when(hotelRepository.existsById(5L)).thenReturn(false);
        assertThatThrownBy(() -> hotelService.deleteById(5L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void changesRating_firstAndSubsequent() {
        // first rating
        Hotel zero = Hotel.builder().id(10L).title("Z").numberRatings(0L).ratings(0L).build();
        when(hotelRepository.findById(10L)).thenReturn(Optional.of(zero));
        when(hotelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RatingChanges rc1 = new RatingChanges(10L, 5L);
        String resp1 = hotelService.changesRating(rc1);
        assertThat(resp1).contains("updated");

        ArgumentCaptor<Hotel> cap1 = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository).save(cap1.capture());
        Hotel saved1 = cap1.getValue();
        assertThat(saved1.getNumberRatings()).isEqualTo(1L);
        assertThat(saved1.getRatings()).isEqualTo(5L);

        // subsequent rating
        Hotel some = Hotel.builder().id(11L).title("S").numberRatings(2L).ratings(4L).build();
        when(hotelRepository.findById(11L)).thenReturn(Optional.of(some));

        RatingChanges rc2 = new RatingChanges(11L, 5L);
        hotelService.changesRating(rc2);

        ArgumentCaptor<Hotel> cap2 = ArgumentCaptor.forClass(Hotel.class);
        verify(hotelRepository, atLeastOnce()).save(cap2.capture());
        List<Hotel> allSaved = cap2.getAllValues();
        Hotel saved2 = allSaved.get(allSaved.size() - 1);
        // currentSum = 4*2=8, newSum=8+5=13, newCount=3 -> avg=round(13/3=4.333)=4
        assertThat(saved2.getNumberRatings()).isEqualTo(3L);
        assertThat(saved2.getRatings()).isEqualTo(4L);
    }

    @Test
    void filtrate_appliesFilters() {
        when(hotelRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(asList(h1, h2)));

        FilterHotelRequest filter = mock(FilterHotelRequest.class);
        when(filter.city()).thenReturn("CityA");
        when(filter.distance()).thenReturn(null);
        when(filter.address()).thenReturn(null);
        when(filter.numberRatings()).thenReturn(null);
        when(filter.headingAdvertisements()).thenReturn(null);
        when(filter.ratings()).thenReturn(null);
        when(filter.title()).thenReturn(null);

        List<HotelResponse> res = hotelService.filtrate(0, 10, filter);
        assertThat(res).hasSize(1);
        assertThat(res.get(0).title()).isEqualTo("Hotel One");
    }
}
