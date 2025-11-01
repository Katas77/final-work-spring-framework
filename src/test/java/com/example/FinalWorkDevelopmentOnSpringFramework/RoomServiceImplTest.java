package com.example.FinalWorkDevelopmentOnSpringFramework;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.impl.RoomServiceImpl;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room1;
    private Room room2;

    @BeforeEach
    void setUp() {
        room1 = Room.builder()
                .id(1L)
                .name("Room A")
                .description("Nice")
                .price(100L)
                .maximumPeople(2L)
                .unavailableBegin(LocalDate.of(2025, 1, 10))
                .unavailableEnd(LocalDate.of(2025, 1, 20))
                .hotel(new Hotel())
                .build();

        room2 = Room.builder()
                .id(2L)
                .name("Room B")
                .description("Ok")
                .price(200L)
                .maximumPeople(4L)
                .unavailableBegin(null)
                .unavailableEnd(null)
                .build();
    }

    @Test
    void findAll_returnsPageContent() {
        when(roomRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(asList(room1, room2)));

        List<Room> result = roomService.findAll(0, 10);

        assertThat(result).containsExactly(room1, room2);
        verify(roomRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void findById_found() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));
        var resp = roomService.findById(1L);
        assertThat(resp).isNotNull();

    }

    @Test
    void findById_notFound_throws() {
        when(roomRepository.findById(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> roomService.findById(5L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void save_callsRepository() {
        when(roomRepository.save(room1)).thenReturn(room1);
        String res = roomService.save(room1);
        assertThat(res).contains("saved");
        verify(roomRepository).save(room1);
    }

    @Test
    void update_existing_updates() {
        Room update = Room.builder().id(1L).name("Room A Updated").build();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room1));
        when(roomRepository.save(any(Room.class))).thenAnswer(inv -> inv.getArgument(0));

        String res = roomService.update(update);

        assertThat(res).contains("updated");
        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo("Room A Updated");
    }

    @Test
    void update_notFound_throws() {
        Room update = Room.builder().id(99L).name("X").build();
        when(roomRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> roomService.update(update)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void delete_existing_deleted() {
        when(roomRepository.existsById(1L)).thenReturn(true);
        doNothing().when(roomRepository).deleteById(1L);

        String res = roomService.deleteById(1L);
        assertThat(res).contains("deleted");
        verify(roomRepository).deleteById(1L);
    }

    @Test
    void delete_notFound_throws() {
        when(roomRepository.existsById(2L)).thenReturn(false);
        assertThatThrownBy(() -> roomService.deleteById(2L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void findFilter_filtersByPriceAndAvailabilityAndOtherFields() {
        // page returns two rooms
        when(roomRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(asList(room1, room2)));

        // Создаём mock FilterRoom, чтобы не зависеть от конструктора
        FilterRoom filter = mock(FilterRoom.class);
        when(filter.dateCheckIn()).thenReturn("150125"); // 15.01.2025 (ddMMyy)
        when(filter.maxPrice()).thenReturn(150L);
        when(filter.minPrice()).thenReturn(50L);
        when(filter.description()).thenReturn(null);
        when(filter.roomId()).thenReturn(null);
        when(filter.maximumPeople()).thenReturn(null);

        // room1 на 15.01.2025 недоступна (10..20) поэтому должна быть отфильтрована
        // room2 доступна и цена 200 > 150 -> отфильтрована по цене
        // В итоге список пуст -> NotFoundException
        assertThatThrownBy(() -> roomService.findFilter(0, 10, filter))
                .isInstanceOf(NotFoundException.class);

        // Снизим maxPrice до 250 — тогда room2 пройдет (price 200)
        when(filter.maxPrice()).thenReturn(250L);
        List<Room> res = roomService.findFilter(0, 10, filter);
        assertThat(res).containsExactly(room2);
    }

    @Test
    void localDateOfString_supportsTwoFormats_andThrowsOnInvalid() {
        LocalDate d1 = roomService.localDateOfString("150125"); // ddMMyy
        assertThat(d1).isEqualTo(LocalDate.of(2025, 1, 15));

        LocalDate d2 = roomService.localDateOfString("15012025"); // ddMMyyyy
        assertThat(d2).isEqualTo(LocalDate.of(2025, 1, 15));

        assertThatThrownBy(() -> roomService.localDateOfString("invalid"))
                .isInstanceOf(BusinessLogicException.class);
    }

    @Test
    void findAll_invalidPageParams_throws() {
        assertThatThrownBy(() -> roomService.findAll(-1, 10)).isInstanceOf(BusinessLogicException.class);
        assertThatThrownBy(() -> roomService.findAll(0, 0)).isInstanceOf(BusinessLogicException.class);
    }
}
