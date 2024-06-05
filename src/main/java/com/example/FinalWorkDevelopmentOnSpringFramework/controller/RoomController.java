


package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.DateFormatException;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.RoomMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UTFDataFormatException;


@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    private final RoomMapper roomMapper;

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<RoomListResponse> findAll(@PathVariable int pageNumber, @PathVariable  int pageSize) {
        return ResponseEntity.ok(roomMapper.roomListResponseList(roomService.findAll(pageNumber,pageSize)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(roomMapper.roomToResponse(roomService.findById(id)));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateRoomRequest request) throws UTFDataFormatException, DateFormatException {
        return roomService.save(roomMapper.createRoomRequestToRoom(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> update( @RequestBody RoomUpdateRequest request) throws UTFDataFormatException, DateFormatException {
        return roomService.update(roomMapper.roomUpdateRequestToRoom(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return roomService.deleteById(id);
    }


}


