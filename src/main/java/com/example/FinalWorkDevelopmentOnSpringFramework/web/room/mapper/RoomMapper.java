package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomUpdateRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    public static Room toEntity(CreateRoomRequest request) {
        Hotel hotel = new Hotel();
        hotel.setId(request.hotelId());
        return Room.builder()
                .name(request.name())
                .description(request.description())
                .number(request.number())
                .price(request.price())
                .maximumPeople(request.maximumPeople())
                .unavailableBegin(parseDate(request.dateBegin()))
                .unavailableEnd(parseDate(request.dateEnd()))
                .hotel(hotel)
                .build();
    }


    public static Room updateFromRequest(RoomUpdateRequest request) {
        if (request == null) return null;
        Hotel hotel = new Hotel();
        hotel.setId(request.hotelId());

        return Room.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .number(request.number())
                .price(request.price())
                .maximumPeople(request.maximumPeople())
                .unavailableBegin(parseDate(request.dateBegin()))
                .unavailableEnd(parseDate(request.dateEnd()))
                .hotel(hotel)
                .build();

    }

    public static RoomResponse toResponse(Room room) {
        if (room == null) return null;

        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getDescription(),
                room.getNumber(),
                room.getPrice(),
                room.getMaximumPeople(),
                room.getUnavailableBegin(),
                room.getUnavailableEnd(),
                room.getHotel().toString()
        );
    }

    public static List<RoomResponse> toResponseList(List<Room> rooms) {
        if (rooms == null) return List.of();
        return rooms.stream()
                .map(RoomMapper::toResponse)
                .collect(Collectors.toList());
    }


    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new BusinessLogicException("Некорректный формат даты: " + dateStr + ". Ожидается формат: ГГГГ-ММ-ДД");
        }
    }
}
