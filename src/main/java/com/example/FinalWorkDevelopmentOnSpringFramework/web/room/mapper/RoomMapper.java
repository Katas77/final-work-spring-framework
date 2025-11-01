package com.example.FinalWorkDevelopmentOnSpringFramework.web.room.mapper;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Hotel;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.RoomUpdateRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RoomMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // CreateRoomRequest → Room
    public static Room toEntity(CreateRoomRequest request) {
        request.validate();

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

    // RoomUpdateRequest → Room (частичное обновление)
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

    // Room → RoomResponse
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

    // List<Room> → List<RoomResponse>
    public static List<RoomResponse> toResponseList(List<Room> rooms) {
        if (rooms == null) return List.of();
        return rooms.stream()
                .map(RoomMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Вспомогательный парсинг
    private static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr.trim(), FORMATTER);
    }
}
