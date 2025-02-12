package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.HotelRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomUpdateRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.RoomMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Component
@Primary
@AllArgsConstructor
public class RoomMapAllField implements RoomMapper {
    private final HotelRepository hotelRepository;

    @Override
    public Room createRoomRequestToRoom(CreateRoomRequest request) throws BusinessLogicException {
        if (request == null) {
            return null;
        }
        Long hotelId = request.getHotelId();
        String unavailableBeginStr = request.getDateBegin();
        String unavailableEndStr = request.getDateEnd();
        var hotel = getHotelOrThrow(hotelId);
        LocalDate unavailableBegin = parseLocalDate(unavailableBeginStr);
        LocalDate unavailableEnd = parseLocalDate(unavailableEndStr);
        return Room.builder()
                .name(request.getName())
                .hotel(hotel)
                .description(request.getDescription())
                .maximumPeople(request.getMaximumPeople())
                .price(request.getPrice())
                .number(request.getNumber())
                .unavailableBegin(unavailableBegin)
                .unavailableEnd(unavailableEnd)
                .build();
    }

    @Override
    public Room roomUpdateRequestToRoom(RoomUpdateRequest request) throws BusinessLogicException {
        if (request == null) {
            return null;
        }
        Long roomId = request.getId();
        Long hotelId = request.getHotelId();
        String unavailableBeginStr = request.getDateBegin();
        String unavailableEndStr = request.getDateEnd();
        var hotel = Optional.ofNullable(hotelId).map(this::getHotelOrThrow).orElse(null);
        LocalDate unavailableBegin = Optional.ofNullable(unavailableBeginStr).map(this::parseLocalDate).orElse(null);
        LocalDate unavailableEnd = Optional.ofNullable(unavailableEndStr).map(this::parseLocalDate).orElse(null);

        return Room.builder()
                .id(roomId)
                .name(request.getName())
                .hotel(hotel)
                .description(request.getDescription())
                .maximumPeople(request.getMaximumPeople())
                .price(request.getPrice())
                .number(request.getNumber())
                .unavailableBegin(unavailableBegin)
                .unavailableEnd(unavailableEnd)
                .build();
    }


    @Override
    public RoomResponse roomToResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .hotel(room.getHotel())
                .description(room.getDescription())
                .maximumPeople(room.getMaximumPeople())
                .price(room.getPrice())
                .number(room.getNumber())
                .dateBegin(room.getUnavailableBegin())
                .dateEnd(room.getUnavailableEnd())
                .build();
    }

    private Hotel getHotelOrThrow(Long id) {
        return hotelRepository.findById(id).orElseThrow(() -> new RuntimeException("Hotel not found!"));
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
