package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.BookingService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.CreateBookingRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.BookingMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.io.UTFDataFormatException;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<BookingListResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(bookingMapper.BookingListResponseList(bookingService.findAll(pageNumber, pageSize)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> findById(@PathVariable long id) {
        return bookingService.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CreateBookingRequest request) throws UTFDataFormatException, BusinessLogicException {
        return bookingService.save(bookingMapper.createBookingToBooking(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody BookingUpdateRequest request) throws UTFDataFormatException, BusinessLogicException {
        return bookingService.update(bookingMapper.bookingUpdateRequestToBooking(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return bookingService.deleteById(id);
    }

}
