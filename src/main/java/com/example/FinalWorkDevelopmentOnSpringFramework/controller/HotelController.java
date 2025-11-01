package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.dto.*;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.hotel.mapper.HotelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @GetMapping("/{pageNumber}/{pageSize}")
    public List<HotelResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return HotelMapper.toResponseList(hotelService.findAll(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public HotelResponse findById(@PathVariable long id) {
        return hotelService.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    public String create(@RequestBody CreateHotelRequest request) {
        return hotelService.save(HotelMapper.toEntity(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping
    public String update(@RequestBody UpdateHotelRequest request) {
        return hotelService.update(HotelMapper.updateFromRequest(request));
    }

    @PostMapping("/{id}/{newMark}")
    public String changesRating(@PathVariable Long id, @PathVariable Long newMark) {
        return hotelService.changesRating(new RatingChanges(id,newMark));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return hotelService.deleteById(id);
    }

    @GetMapping("/filter/{pageNumber}/{pageSize}")
    public List<HotelResponse>findFilter(@PathVariable int pageNumber, @PathVariable int pageSize, @RequestBody FilterHotelRequest request) {
        return hotelService.filtrate(pageNumber, pageSize, request);
    }

}
