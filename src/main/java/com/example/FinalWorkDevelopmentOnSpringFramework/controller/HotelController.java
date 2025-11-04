package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.CustomValid;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.*;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.mapper.HotelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<List<HotelResponse>> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(HotelMapper.toResponseList(hotelService.findAll(pageNumber, pageSize)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(hotelService.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    @CustomValid
    public ResponseEntity<String> create(@RequestBody CreateHotelRequest request) {
        return ResponseEntity.ok(hotelService.save(HotelMapper.toEntity(request)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody UpdateHotelRequest request) {
        return ResponseEntity.ok(hotelService.update(HotelMapper.updateFromRequest(request)));
    }

    @PostMapping("/{id}/{newMark}")
    public ResponseEntity<String> changesRating(@PathVariable Long id, @PathVariable Long newMark) {
        return ResponseEntity.ok(hotelService.changesRating(new RatingChanges(id, newMark)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.deleteById(id));
    }

    @GetMapping("/filter/{pageNumber}/{pageSize}")
    public ResponseEntity<List<HotelResponse>> findFilter(
            @PathVariable int pageNumber,
            @PathVariable int pageSize,
            @RequestBody FilterHotelRequest request) {
        return ResponseEntity.ok(hotelService.filtrate(pageNumber, pageSize, request));
    }
}