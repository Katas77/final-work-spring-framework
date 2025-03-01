package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.RoomRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.BookingUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.booking.CreateBookingRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.BookingMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.io.UTFDataFormatException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@Primary
@AllArgsConstructor
public class BookingMapAllField implements BookingMapper {
    private final UserService userService;
    private final RoomRepository roomRepository;

    public Booking createBookingToBooking(CreateBookingRequest request) throws BusinessLogicException {
        if (request == null) {
            return null;
        }
       long userId = request.getUserId();
       long roomId = request.getRoomId();
        String checkInDateStr = request.getDateCheck_in();
        String checkOutDateStr = request.getDateCheck_out();
        var user = getUserOrThrow(userId);
        var room = getRoomOrThrow(roomId);
        LocalDate checkInDate = parseLocalDate(checkInDateStr);
        LocalDate checkOutDate = parseLocalDate(checkOutDateStr);
        return Booking.builder()
                .room(room)
                .user(user)
                .dateCheck_in(checkInDate)
                .dateCheck_out(checkOutDate)
                .build();
    }

    @Override
    public Booking bookingUpdateRequestToBooking(BookingUpdateRequest request) throws UTFDataFormatException, BusinessLogicException {
        if (request == null) {
            return null;
        }

        Long bookingId = request.getBookingId();
        Long userId = request.getUserId();
        Long roomId = request.getRoomId();
        String checkInDateStr = request.getDateCheck_in();
        String checkOutDateStr = request.getDateCheck_out();

        // Получаем объекты User и Room
        var user = Optional.ofNullable(userId).map(this::getUserOrThrow).orElse(null);
        var room = Optional.ofNullable(roomId).map(this::getRoomOrThrow).orElse(null);

        // Преобразуем даты в формат LocalDate
        LocalDate checkInDate = Optional.ofNullable(checkInDateStr).map(this::parseLocalDate).orElse(null);
        LocalDate checkOutDate = Optional.ofNullable(checkOutDateStr).map(this::parseLocalDate).orElse(null);

        // Создаём новый объект Booking
        return Booking.builder()
                .id(bookingId)
                .room(room)
                .user(user)
                .dateCheck_in(checkInDate)
                .dateCheck_out(checkOutDate)
                .build();
    }



    @Override
    public BookingResponse BookingToResponse(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .room(booking.getRoom())
                .userName(booking.getUser().getName())
                .dateCheck_in(booking.getDateCheck_in())
                .dateCheck_out(booking.getDateCheck_out())
                .build();
    }
    private Room getRoomOrThrow(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found!"));
    }
    private User getUserOrThrow(Long id) {
        return userService.findById(id);
    }
    /**
     * Метод для парсинга строки в объект LocalDate.
     *
     * @param date строка с датой в формате 'DDMMYY'
     * @return объект LocalDate
     */
    private LocalDate parseLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        return LocalDate.parse(date, formatter);
    }

}
