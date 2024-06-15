
package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.impl;


import com.example.FinalWorkDevelopmentOnSpringFramework.exception.DateFormatException;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Room;
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

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-03-30T17:35:46+0300",
        comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)

@Component
@Primary
@AllArgsConstructor
public class RoomMapAllField implements RoomMapper {
    private final HotelRepository hotelRepository;

    @Override
    public Room createRoomRequestToRoom(CreateRoomRequest request) throws DateFormatException {
        if (request == null) {
            return null;
        }
        return Room.builder()
                .name(request.getName())
                .hotel(hotelRepository.findById(request.getHotelId()).orElseThrow(() -> new RuntimeException("hotel not found!")))
                .description(request.getDescription())
                .maximumPeople(request.getMaximumPeople())
                .price(request.getPrice())
                .number(request.getNumber())
                .unavailableBegin(localDateOfString(request.getDateBegin()))
                .unavailableEnd(localDateOfString(request.getDateEnd()))
                .build();
    }

    @Override
    public Room roomUpdateRequestToRoom(RoomUpdateRequest request) throws DateFormatException {
        if (request == null) {
            return null;
        }
        return Room.builder()
                .id(request.getId())
                .name(request.getName())
                .hotel(request.getHotelId() == null ? null : hotelRepository.findById(request.getHotelId()).orElseThrow(() -> new RuntimeException("hotel not found!")))
                .description(request.getDescription())
                .maximumPeople(request.getMaximumPeople())
                .price(request.getPrice())
                .number(request.getNumber())
                .unavailableBegin(request.getDateBegin() == null ? null : localDateOfString(request.getDateBegin()))
                .unavailableEnd(request.getDateEnd() == null ? null : localDateOfString(request.getDateEnd()))
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


    public LocalDate localDateOfString(String date) throws DateFormatException {
        String[] arrayDate = date.split("");
        if (arrayDate.length != 6) {
            throw new DateFormatException("Enter date in DDMMYY format. Example - 221124");
        }
        String yearSt = "20" + arrayDate[4] + arrayDate[5];
        int year = Integer.parseInt(yearSt);
        String monthSt = arrayDate[2] + arrayDate[3];
        int month = Integer.parseInt(monthSt);
        String daySt = arrayDate[0] + arrayDate[1];
        int day = Integer.parseInt(daySt);
        return LocalDate.of(year, month, day);
    }
}



