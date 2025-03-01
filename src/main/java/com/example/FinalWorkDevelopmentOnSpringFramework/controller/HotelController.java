package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.*;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.HotelMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<HotelListResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(hotelMapper.hotelListResponseList(hotelService.findAll(pageNumber, pageSize)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable long id) {
        return hotelService.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateHotelRequest request) {
        return hotelService.save(hotelMapper.createHotelRequestToHotel(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody UpdateHotelRequest request) {
        return hotelService.update(hotelMapper.updateHotelRequestToHotel(request));
    }

    @PostMapping("/{id}/{newMark}")
    public ResponseEntity<String> changesRating(@PathVariable Long id, @PathVariable Long newMark) {
        return hotelService.changesRating(RatingChanges.builder()
                .id(id)
                .newMark(newMark)
                .build());
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return hotelService.deleteById(id);
    }

    @GetMapping("/filter/{pageNumber}/{pageSize}")
    public ResponseEntity<HotelListResponse> findFilter(@PathVariable int pageNumber, @PathVariable int pageSize, @RequestBody FilterHotel request) {
        return hotelService.filtrate(pageNumber, pageSize, request);
    }

}
