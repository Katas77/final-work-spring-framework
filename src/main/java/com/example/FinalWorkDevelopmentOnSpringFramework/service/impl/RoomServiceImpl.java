
package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.utils.BeanUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;



    @Override
    public List<Room> findAll(int pageNumber, int pageSize) {
        return roomRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Room with ID {0} not found", id)));
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
                    .body(MessageFormat.format("Room with ID {0} not found", room.getId()));
        }
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



}

