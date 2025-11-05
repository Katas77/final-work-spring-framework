package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.utils.BeanUtils;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelService hotelService;

    @Override
    public List<Room> findAll(int pageNumber, int pageSize) {
        validatePageParams(pageNumber, pageSize);
        log.info("Fetching all rooms, page: {}, size: {}", pageNumber, pageSize);
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return roomRepository.findAll(page).getContent();
    }

    @Override
    public Room findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        log.info("Finding room with ID {}", id);
        return roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Room with ID {0} not found", id)));
    }

    @Override
    public String save(Room room) {
        room.setHotel(hotelService.findById(room.getId()));
        log.info("Saving room with name {}", room.getName());
        roomRepository.save(room);
        return MessageFormat.format("Room with name - {0} saved", room.getName());
    }

    @Transactional
    @Override
    public String update(Room room) {
        log.info("Updating room with ID {}", room.getId());
        Room existingRoom = this.findById(room.getId());
        BeanUtils.copyNonNullProperties(room, existingRoom);
        existingRoom.setHotel(hotelService.findById(room.getHotel().getId()));
        roomRepository.save(existingRoom);
        return MessageFormat.format("Room with ID {0} updated", room.getId());
    }
    @Override
    public List<Room> findFilter(int pageNumber, int pageSize, FilterRoom filter) {

        LocalDate in = parseDateOrNull(filter.dateCheckIn());
        LocalDate out = parseDateOrNull(filter.dateCheckOut());

        Page<Room> page = roomRepository.findFilter(
                toPattern(filter.description()),
                filter.minPrice(),
                filter.maxPrice(),
                filter.maximumPeople(),
                in,
                out,
                filter.roomId(),
                PageRequest.of(Math.max(0, pageNumber), Math.max(1, pageSize))
        );

        return page.getContent();
    }

    private LocalDate parseDateOrNull(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private String toPattern(String input) {
        return (input == null || input.isBlank()) ? null : "%" + input.toLowerCase() + "%";
    }
    @Transactional
    @Override
    public String deleteById(Long id) {
        log.info("Deleting room with ID {}", id);
        if (!roomRepository.existsById(id)) {
            throw new NotFoundException(MessageFormat.format("Room with ID {0} not found", id));
        }
        roomRepository.deleteById(id);
        return MessageFormat.format("Room with ID {0} deleted", id);
    }



    private void validatePageParams(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new BusinessLogicException("Invalid page parameters");
        }
    }

    private String toPattern2(String s) {
        return (s == null || s.isBlank()) ? null : "%" + s.toLowerCase() + "%";
    }


    }




