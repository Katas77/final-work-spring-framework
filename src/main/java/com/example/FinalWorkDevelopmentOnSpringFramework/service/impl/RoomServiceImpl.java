package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.DateFormatException;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Room;
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
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    @Override
    public ResponseEntity<String> save(Room room) {
        roomRepository.save(room);
        return ResponseEntity.ok(MessageFormat.format("Room with name -  {0} save", room.getName()));
    }

    @Transactional
    @Override
    public ResponseEntity<String> update(Room room) {
        Optional<Room> existedRoom = roomRepository.findById(room.getId());
        if (existedRoom.isPresent()) {
            BeanUtils.copyNonNullProperties(room, existedRoom.get());
            roomRepository.save(existedRoom.get());
            return ResponseEntity.ok(MessageFormat.format("Room with ID {0} updated", room.getId()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Room with ID {0} not found", room.getId()));}
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteById(Long id) {
        Optional<Room> newsRepositoryById = roomRepository.findById(id);
        if (newsRepositoryById.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("Room with ID {0} not found", id));
        } else {
            roomRepository.deleteById(id);
            return ResponseEntity.ok(MessageFormat.format("Room with ID {0} deleted", id));
        }
    }

    @Override
    public ResponseEntity<RoomListResponse> findFilter(int pageNumber, int pageSize, FilterRoom request) {
        if (request.getDateCheck_in() == null | request.getDateCheck_out() == null) {
            log.info("Both dates must be selected.   Select check-in and check-out dates.");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        List<Room> roomList = roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent().stream()
                .filter(room -> {
                    try {
                        return !notOnTheseDates2(localDateOfString(request.getDateCheck_in()), room);
                    } catch (DateFormatException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(room -> {
                    try {
                        return !notOnTheseDates2(localDateOfString(request.getDateCheck_out()), room);
                    } catch (DateFormatException e) {
                        throw new RuntimeException(e);
                    }})
                .filter(room -> request.getMaxPrice() == null || request.getMaxPrice().compareTo(room.getPrice()) >= 0)
                .filter(room -> request.getMinPrice() == null || request.getMinPrice().compareTo(room.getPrice()) <= 0)
                .filter(room -> request.getDescription() == null | room.getDescription().equals(request.getDescription()))
                .filter(room -> request.getRoomId() == null | room.getId().equals(request.getRoomId()))
                .filter(room -> request.getMaximumPeople() == null || room.getMaximumPeople().equals(request.getMaximumPeople()))
                .collect(Collectors.toList());
        if (roomList.isEmpty()) {
            log.info("No room with these parameters was found");
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(roomMapper.roomListResponseList(roomList));
    }

    boolean notOnTheseDates2(LocalDate localDate, Room room) {
        return !(localDate.isBefore(room.getUnavailableBegin()) || localDate.isAfter(room.getUnavailableEnd()));
    }

    public LocalDate localDateOfString(String date) throws DateFormatException {
        String[] arrayDate = date.split("");
        if (arrayDate.length != 6) {
            throw new DateFormatException("Enter date in DDMMYY format. Example - 221124");
        }
        String yearSt = "20" + arrayDate[4] + arrayDate[5];
        int year = Integer.parseInt(yearSt);
        String monthSt = arrayDate[2] + arrayDate[3];
        int month = Integer.parseInt(monthSt);
        String daySt = arrayDate[0] + arrayDate[1];
        int day = Integer.parseInt(daySt);
        return LocalDate.of(year, month, day);
    }

}
