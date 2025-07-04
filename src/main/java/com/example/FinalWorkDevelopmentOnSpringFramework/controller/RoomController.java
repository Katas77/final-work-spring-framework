package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.*;
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
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<RoomListResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(roomMapper.roomListResponseList(roomService.findAll(pageNumber, pageSize)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable long id) {return ResponseEntity.ok(roomService.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateRoomRequest request) throws UTFDataFormatException, BusinessLogicException {
        return ResponseEntity.ok(roomService.save(roomMapper.createRoomRequestToRoom(request)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody RoomUpdateRequest request) throws UTFDataFormatException, BusinessLogicException {
        return ResponseEntity.ok( roomService.update(roomMapper.roomUpdateRequestToRoom(request)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return  ResponseEntity.ok(roomService.deleteById(id));
    }

    @GetMapping("/filter/{pageNumber}/{pageSize}")
    public ResponseEntity<RoomListResponse> findFilterRoom(@PathVariable int pageNumber, @PathVariable int pageSize, @RequestBody FilterRoom request) {
        return  ResponseEntity.ok(roomService.findFilter(pageNumber, pageSize, request));
    }
}
