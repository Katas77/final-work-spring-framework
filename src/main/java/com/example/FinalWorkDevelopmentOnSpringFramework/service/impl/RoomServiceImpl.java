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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Override
    public List<Room> findAll(int pageNumber, int pageSize) {
        return roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public ResponseEntity<RoomResponse> findById(Long id) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(roomMapper.roomToResponse(optionalRoom.get()));
        } else
            log.info(MessageFormat.format("Room with ID {0} not found", id));
        return ResponseEntity.notFound().build();

    }

    @Override
    public String save(Room room) {
        roomRepository.save(room);
        return MessageFormat.format("Room with name -  {0} save", room.getName());
    }

    @Transactional
    @Override
    public String update(Room room) {
        Optional<Room> existedRoom = roomRepository.findById(room.getId());
        if (existedRoom.isPresent()) {
            BeanUtils.copyNonNullProperties(room, existedRoom.get());
            roomRepository.save(existedRoom.get());
            return MessageFormat.format("Room with ID {0} updated", room.getId());
        } else {
            throw new NotFoundException("Room with ID   " + room.getId() + "  not found");

        }
    }

    @Transactional
    @Override
    public String deleteById(Long id) {
        Optional<Room> newsRepositoryById = roomRepository.findById(id);
        if (newsRepositoryById.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("Room with ID {0} not found", id));
        } else {
            roomRepository.deleteById(id);
            return MessageFormat.format("Room with ID {0} deleted", id);
        }
    }

    @Override
    public RoomListResponse findFilter(int pageNumber, int pageSize, FilterRoom request) {
        if (request.getDateCheck_in() == null | request.getDateCheck_out() == null) {
            log.info("Both dates must be selected. Select check-in and check-out dates.");
            throw new BadRequestException("Both dates must be selected. Select check-in and check-out dates.");
        }

        List<Room> roomList = roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                .filter(room -> notOnTheseDates(localDateOfString(request.getDateCheck_in()), room))
                .filter(room -> request.getMaxPrice() == null || request.getMaxPrice().compareTo(room.getPrice()) >= 0)
                .filter(room -> request.getMinPrice() == null || request.getMinPrice().compareTo(room.getPrice()) <= 0)
                .filter(room -> request.getDescription() == null | room.getDescription().equals(request.getDescription()))
                .filter(room -> request.getRoomId() == null | room.getId().equals(request.getRoomId()))
                .filter(room -> request.getMaximumPeople() == null || room.getMaximumPeople().equals(request.getMaximumPeople()))
                .collect(Collectors.toList());
        if (roomList.isEmpty()) {
            log.info("No room with these parameters was found");
            throw new NotFoundException("No room with these parameters was found");
        }
        return roomMapper.roomListResponseList(roomList);
    }

    boolean notOnTheseDates(LocalDate localDate, Room room) {
        return localDate.isBefore(room.getUnavailableBegin()) || localDate.isAfter(room.getUnavailableEnd());
    }

    public LocalDate localDateOfString(String date) throws BusinessLogicException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        return LocalDate.parse(date, formatter);
    }

}
