package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.CustomValid;
import com.example.FinalWorkDevelopmentOnSpringFramework.security.AppUserPrincipal;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.BookingService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.CreateBookingRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<List<BookingResponse>> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(BookingMapper.toResponseList(bookingService.findAll(pageNumber, pageSize)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> findById(@PathVariable long id) {
        return ResponseEntity.ok(BookingMapper.toResponse(bookingService.findById(id)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    @CustomValid
    public ResponseEntity<String> create(@RequestBody CreateBookingRequest request,@AuthenticationPrincipal AppUserPrincipal userDetails) {
        return ResponseEntity.ok(bookingService.save(BookingMapper.toEntity(request,userDetails)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping
    public ResponseEntity<String> update(@RequestBody BookingUpdateRequest request) {
        return ResponseEntity.ok(bookingService.update(BookingMapper.updateFromRequest(request)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.deleteById(id));
    }
}