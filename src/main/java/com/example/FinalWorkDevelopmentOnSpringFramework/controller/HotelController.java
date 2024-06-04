

package com.example.FinalWorkDevelopmentOnSpringFramework.controller;


import com.example.FinalWorkDevelopmentOnSpringFramework.service.HotelService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.HotelResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.CreateHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.hotel.UpdateHotelRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.HotelMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<HotelListResponse> findAll(@PathVariable int pageNumber, @PathVariable  int pageSize) {
        return ResponseEntity.ok(hotelMapper.hotelListResponseList(hotelService.findAll(pageNumber,pageSize)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(hotelMapper.hotelToResponse(hotelService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateHotelRequest request) {
        return hotelService.save(hotelMapper.createHotelRequestToHotel(request));
    }


    @PutMapping
    public ResponseEntity<String> update( @RequestBody UpdateHotelRequest request) {
        return hotelService.update(hotelMapper.updateHotelRequestToHotel(request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return hotelService.deleteById(id);
    }
}


