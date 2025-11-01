package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.service.RoomService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public List<RoomResponse>findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return RoomMapper.toResponseList(roomService.findAll(pageNumber, pageSize));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public RoomResponse findById(@PathVariable long id) {return roomService.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public String  create(@RequestBody CreateRoomRequest request)  {
        return roomService.save(RoomMapper.toEntity(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public String update(@RequestBody RoomUpdateRequest request)  {
        return roomService.update(RoomMapper.updateFromRequest(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String  delete(@PathVariable Long id) {
        return roomService.deleteById(id);
    }

    @GetMapping("/filter/{pageNumber}/{pageSize}")
    public List <RoomResponse>findFilterRoom(@PathVariable int pageNumber, @PathVariable int pageSize, @RequestBody FilterRoom request) {
        return RoomMapper.toResponseList(roomService.findFilter(pageNumber, pageSize, request)) ;
    }
}
