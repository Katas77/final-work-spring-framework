package com.example.FinalWorkDevelopmentOnSpringFramework;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.impl.RoomServiceImpl;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private HotelService hotelService;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void findAll_shouldReturnRooms() {
        Page<Room> page = new PageImpl<>(List.of(new Room()));
        when(roomRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<Room> result = roomService.findAll(0, 10);
        assertThat(result).hasSize(1);
    }

    @Test
    void findById_existing_shouldReturnRoom() {
        Room room = new Room();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        assertThat(roomService.findById(1L)).isSameAs(room);
    }

    @Test
    void save_shouldSaveAndReturnMessage() {
        Room room = Room.builder().name("Deluxe").build();
        when(roomRepository.save(room)).thenReturn(room);

        String result = roomService.save(room);
        assertThat(result).isEqualTo("Room with name - Deluxe saved");
    }

    @Test
    void update_shouldCopyPropertiesAndSave() {
        Room input = Room.builder().id(1L).name("Updated").build();
        Room existing = Room.builder().id(1L).name("Old").hotel(new Hotel()).build();
        Hotel newHotel = Hotel.builder().id(2L).build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(hotelService.findById(2L)).thenReturn(newHotel);
        when(roomRepository.save(existing)).thenReturn(existing);

        input.setHotel(newHotel);
        String result = roomService.update(input);

        assertThat(result).isEqualTo("Room with ID 1 updated");
        assertThat(existing.getName()).isEqualTo("Updated");
        assertThat(existing.getHotel()).isSameAs(newHotel);
    }

    @Test
    void findFilter_withDates_shouldCallRepository() {
        FilterRoom filter = new FilterRoom("view", 10000L, 30000L, 2L, "2025-06-01", "2025-06-10", 5L);
        Page<Room> page = new PageImpl<>(List.of(new Room()));
        when(roomRepository.findFilter(any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        List<Room> result = roomService.findFilter(0, 10, filter);
        assertThat(result).hasSize(1);
        verify(roomRepository).findFilter(
                "%view%", 10000L, 30000L, 2L,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 10),
                5L,
                PageRequest.of(0, 10)
        );
    }

    @Test
    void deleteById_existing_shouldDelete() {
        when(roomRepository.existsById(1L)).thenReturn(true);
        String result = roomService.deleteById(1L);
        assertThat(result).isEqualTo("Room with ID 1 deleted");
        verify(roomRepository).deleteById(1L);
    }
}