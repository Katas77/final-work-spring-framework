package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.Trackable;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BadRequestException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.BookingRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.BookingService;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer.ServiceProducer;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model.BookingEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.BookingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookRepository;
    private final ServiceProducer producer;
    private final BookingMapper bookingMapper;

    @Override
    public List<Booking> findAll(int pageNumber, int pageSize) {
        return bookRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public BookingResponse findById(Long id) {
        Booking booking = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));
        return bookingMapper.BookingToResponse(booking);
    }

    @Override
    public String save(Booking booking) {
        validateDates(booking);
        if (notOnTheseDates(booking.getDateCheck_in(), booking.getRoom())) {
            throw new BadRequestException("Check-in from " + booking.getRoom().getUnavailableBegin() +
                    " to " + booking.getRoom().getUnavailableEnd() + " is not possible");
        }
        if (notOnTheseDates(booking.getDateCheck_out(), booking.getRoom())) {
            throw new BadRequestException("Check-out from " + booking.getRoom().getUnavailableBegin() +
                    " to " + booking.getRoom().getUnavailableEnd() + " is not possible");
        }
        bookRepository.save(booking);
        sendBookingEvent(booking);
   return MessageFormat.format("Booking with ID {0} save", booking.getId());}

    @Override
    public String update(Booking booking) {
        Booking existingBooking = bookRepository.findById(booking.getId())
                .orElseThrow(() -> new NotFoundException("Booking with ID " + booking.getId() + " not found"));
        copyNonNullProperties(booking, existingBooking);
        save(existingBooking);
        return MessageFormat.format("Booking with ID {0} save", booking.getId()); }

    @Override
    public String deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Booking with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
        return MessageFormat.format("Booking with ID {0} delete", id); }

    private void validateDates(Booking booking) {
        if (booking.getDateCheck_in().isBefore(LocalDate.now()) ||
                booking.getDateCheck_out().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Selected dates are already past tense");
        }

        if (booking.getDateCheck_in().isAfter(booking.getDateCheck_out())) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        if (booking.getDateCheck_in().equals(booking.getDateCheck_out())) {
            throw new IllegalArgumentException("Indicate different check-in and check-out dates");
        }
    }

    private void copyNonNullProperties(Booking source, Booking target) {
        if (source.getDateCheck_in() != null) {
            target.setDateCheck_in(source.getDateCheck_in());
        }
        if (source.getDateCheck_out() != null) {
            target.setDateCheck_out(source.getDateCheck_out());
        }
        if (source.getUser() != null) {
            target.setUser(source.getUser());
        }
        if (source.getRoom() != null) {
            target.setRoom(source.getRoom());
        }
    }

    private void sendBookingEvent(Booking booking) {
        BookingEvent bookingEvent = BookingEvent.builder()
                .recordingFacts(String.valueOf(LocalDateTime.now()))
                .UserId(booking.getUser().getId())
                .dateCheck_in(String.valueOf(booking.getDateCheck_in()))
                .dateCheck_out(String.valueOf(booking.getDateCheck_out()))
                .build();
        producer.sendBookingEvent(bookingEvent);
    }

    private boolean notOnTheseDates(LocalDate localDate, Room room) {
        return !(localDate.isBefore(room.getUnavailableBegin()) || localDate.isAfter(room.getUnavailableEnd()));
    }
}