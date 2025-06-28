package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BadRequestException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.utils.BeanUtils;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.FilterRoom;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.RoomMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

import java.util.stream.Collectors;
@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public List<Room> findAll(int pageNumber, int pageSize) {
        log.info("Fetching all rooms, page: {}, size: {}", pageNumber, pageSize);
        return roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public RoomResponse findById(Long id) {
        log.info("Finding room with ID {}", id);
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Room with ID {0} not found", id)));
        return roomMapper.roomToResponse(room);
    }

    @Override
    public String save(Room room) {
        log.info("Saving room with name {}", room.getName());
        roomRepository.save(room);
        return MessageFormat.format("Room with name - {0} saved", room.getName());
    }

    @Transactional
    @Override
    public String update(Room room) {
        log.info("Updating room with ID {}", room.getId());
        Room existingRoom = roomRepository.findById(room.getId())
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("Room with ID {0} not found", room.getId())));
        BeanUtils.copyNonNullProperties(room, existingRoom);
        roomRepository.save(existingRoom);
        return MessageFormat.format("Room with ID {0} updated", room.getId());
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

    @Override
    public RoomListResponse findFilter(int pageNumber, int pageSize, FilterRoom request) {
        if (request.getDateCheck_in() == null || request.getDateCheck_out() == null) {
            log.info("Both dates must be selected");
            throw new BadRequestException("Both dates must be selected. Select check-in and check-out dates.");
        }

        List<Room> roomList = roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                .filter(room -> isDateAvailable(localDateOfString(request.getDateCheck_in()), room))
                .filter(room -> request.getMaxPrice() == null || request.getMaxPrice().compareTo(room.getPrice()) >= 0)
                .filter(room -> request.getMinPrice() == null || request.getMinPrice().compareTo(room.getPrice()) <= 0)
                .filter(room -> request.getDescription() == null || Objects.equals(room.getDescription(), request.getDescription()))
                .filter(room -> request.getRoomId() == null || Objects.equals(room.getId(), request.getRoomId()))
                .filter(room -> request.getMaximumPeople() == null || Objects.equals(room.getMaximumPeople(), request.getMaximumPeople()))
                .collect(Collectors.toList());

        if (roomList.isEmpty()) {
            log.info("No room with these parameters was found");
            throw new NotFoundException("No room with these parameters was found");
        }

        return roomMapper.roomListResponseList(roomList);
    }

    private boolean isDateAvailable(LocalDate date, Room room) {
        return !date.isBefore(room.getUnavailableBegin()) && !date.isAfter(room.getUnavailableEnd());
    }

    public LocalDate localDateOfString(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            log.error("Error parsing date string: {}", date, e);
            throw new BusinessLogicException("Invalid date format: " + date);
        }
    }
}
