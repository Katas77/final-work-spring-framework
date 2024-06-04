
package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.DateFormatException;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.BookingService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.CreateBookingRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.BookingMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UTFDataFormatException;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<BookingListResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(bookingMapper.BookingListResponseList(bookingService.findAll(pageNumber, pageSize)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(bookingMapper.BookingToResponse(bookingService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateBookingRequest request) throws UTFDataFormatException, DateFormatException {
        return bookingService.save(bookingMapper.createBookingToRoom(request));
    }


    @PutMapping
    public ResponseEntity<String> update(@RequestBody BookingUpdateRequest request) throws UTFDataFormatException, DateFormatException {
        return bookingService.update(bookingMapper.bookingUpdateRequestToRoom(request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return bookingService.deleteById(id);
    }


}

