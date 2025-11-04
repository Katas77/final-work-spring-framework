package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BadRequestException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.BookingRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.BookingService;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer.ServiceProducer;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.dto.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.booking.mapper.BookingMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookRepository;
    private final ServiceProducer producer;

    @Override
    public List<Booking> findAll(int pageNumber, int pageSize) {
        validatePageParams(pageNumber, pageSize);
        log.info("Fetching all bookings, page: {}, size: {}", pageNumber, pageSize);
        return bookRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public BookingResponse findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        log.info("Finding booking with ID {}", id);
        Booking booking = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking with ID " + id + " not found"));
        return BookingMapper.toResponse(booking);
    }

    @Override
    public String save(Booking booking) {
        log.info("Saving new booking for user ID {}", booking != null && booking.getUser() != null ? booking.getUser().getId() : "null");
        validateBookingInput(booking);

        // Проверяем перекрытие периода бронирования с недоступностью комнаты
        if (isBookingOverlappingUnavailable(booking.getDateCheck_in(), booking.getDateCheck_out(), booking.getRoom())) {
            Room r = booking.getRoom();
            throw new BadRequestException("Booking interval overlaps room unavailable period: " +
                    r.getUnavailableBegin() + " - " + r.getUnavailableEnd());
        }

        Booking saved = bookRepository.save(booking);
        sendBookingEvent(saved);
        log.info("Booking with ID {} saved successfully", saved.getId());
        return MessageFormat.format("Booking with ID {0} saved", saved.getId());
    }

    @Override
    public String update(Booking booking) {
        log.info("Updating booking with ID {}", booking.getId());
        Booking existingBooking = bookRepository.findById(booking.getId())
                .orElseThrow(() -> new NotFoundException("Booking with ID " + booking.getId() + " not found"));

        copyNonNullProperties(booking, existingBooking);
        validateBookingInput(existingBooking);

        if (isBookingOverlappingUnavailable(existingBooking.getDateCheck_in(), existingBooking.getDateCheck_out(), existingBooking.getRoom())) {
            Room r = existingBooking.getRoom();
            throw new BadRequestException("Updated booking interval overlaps room unavailable period: " +
                    r.getUnavailableBegin() + " - " + r.getUnavailableEnd());
        }

        Booking saved = bookRepository.save(existingBooking);
        log.info("Booking with ID {} updated successfully", saved.getId());
        return MessageFormat.format("Booking with ID {0} updated", saved.getId());
    }

    @Override
    public String deleteById(Long id) {
        log.info("Deleting booking with ID {}", id);
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("Booking with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
        log.info("Booking with ID {} deleted successfully", id);
        return MessageFormat.format("Booking with ID {0} deleted", id);
    }

    // --- вспомогательные методы ---

    private void validatePageParams(int pageNumber, int pageSize) {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new BadRequestException("Invalid page parameters");
        }
    }

    private void validateBookingInput(Booking booking) {
        if (booking == null) {
            throw new BadRequestException("Booking is null");
        }
        if (booking.getUser() == null || booking.getUser().getId() == null) {
            throw new BadRequestException("Booking must contain a valid user");
        }
        if (booking.getRoom() == null || booking.getRoom().getId() == null) {
            throw new BadRequestException("Booking must contain a valid room");
        }
        if (booking.getDateCheck_in() == null || booking.getDateCheck_out() == null) {
            throw new BadRequestException("Booking dates must not be null");
        }

        LocalDate today = LocalDate.now();
        if (booking.getDateCheck_in().isBefore(today) || booking.getDateCheck_out().isBefore(today)) {
            throw new BadRequestException("Selected dates are already past");
        }
        if (booking.getDateCheck_in().isAfter(booking.getDateCheck_out())) {
            throw new BadRequestException("Check-in date must be before check-out date");
        }
        if (booking.getDateCheck_in().equals(booking.getDateCheck_out())) {
            throw new BadRequestException("Check-in and check-out dates must differ");
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
        producer.sendBookingEvent(booking);
    }

    /**
     * Проверяет, пересекается ли интервал бронирования [start, end] (включительно)
     * с периодом недоступности комнаты room.unavailableBegin .. room.unavailableEnd.
     * Null значения unavailableBegin/unavailableEnd интерпретируются как "открытый" конец:
     * - оба null => нет недоступности
     * - unavailableBegin != null, unavailableEnd == null => недоступно с unavailableBegin и далее
     * - unavailableBegin == null, unavailableEnd != null => недоступно до unavailableEnd включительно
     */
    private boolean isBookingOverlappingUnavailable(LocalDate start, LocalDate end, Room room) {
        if (room == null) {
            return false;
        }
        LocalDate ub = room.getUnavailableBegin();
        LocalDate ue = room.getUnavailableEnd();

        // Нет периода недоступности
        if (ub == null && ue == null) {
            return false;
        }

        // Общая проверка перекрытия двух отрезков [start, end] и [ub, ue]

        boolean endsBeforeUnavailableStart = (ue == null) ? false : end.isBefore(ub == null ? LocalDate.MIN : ub);
        boolean startsAfterUnavailableEnd = (ub == null) ? false : start.isAfter(ue == null ? LocalDate.MAX : ue);

        // Если один отрезок полностью до другого => нет пересечения
        if (end.isBefore(ub == null ? LocalDate.MIN : ub) || start.isAfter(ue == null ? LocalDate.MAX : ue)) {
            return false;
        }
        // Иначе есть пересечение
        return true;
    }
}
