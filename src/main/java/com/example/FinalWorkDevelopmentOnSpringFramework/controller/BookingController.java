package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.service.BookingService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.CreateBookingRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public List<BookingResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return BookingMapper.toResponseList(bookingService.findAll(pageNumber, pageSize)) ;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public BookingResponse findById(@PathVariable long id) {
        return (bookingService.findById(id));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping
    public String create(@RequestBody CreateBookingRequest request)  {
        return bookingService.save(BookingMapper.toEntity(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping
    public String update(@RequestBody BookingUpdateRequest request) {
        return bookingService.update(BookingMapper.updateFromRequest(request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return bookingService.deleteById(id);
    }

}
