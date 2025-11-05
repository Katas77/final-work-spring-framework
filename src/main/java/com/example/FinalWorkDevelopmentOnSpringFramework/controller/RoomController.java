package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.CustomValid;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<List<RoomResponse>> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(RoomMapper.toResponseList(roomService.findAll(pageNumber, pageSize)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(RoomMapper.toResponse(roomService.findById(id)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    @CustomValid
    public ResponseEntity<String> create(@RequestBody CreateRoomRequest request) {
        return ResponseEntity.ok(roomService.save(RoomMapper.toEntity(request)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody RoomUpdateRequest request) {
        return ResponseEntity.ok(roomService.update(RoomMapper.updateFromRequest(request)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.deleteById(id));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @CustomValid
    @GetMapping("/filter/{pageNumber}/{pageSize}")
    public ResponseEntity<List<RoomResponse>> findFilterRoom(@PathVariable int pageNumber, @PathVariable int pageSize, @RequestBody FilterRoom request) {
        return ResponseEntity.ok(RoomMapper.toResponseList(roomService.findFilter(pageNumber, pageSize, request)));
    }
}