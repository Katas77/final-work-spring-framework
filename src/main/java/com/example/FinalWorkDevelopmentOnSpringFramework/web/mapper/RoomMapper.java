package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.DateFormatException;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import java.io.UTFDataFormatException;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    RoomMapper INSTANCE = Mappers.getMapper(RoomMapper.class);

    @Mapping(target = "id", ignore = true)
    Room createRoomRequestToRoom(CreateRoomRequest request) throws UTFDataFormatException, DateFormatException;

    @Mapping(target = "id", ignore = true)
    Room roomUpdateRequestToRoom(RoomUpdateRequest request) throws UTFDataFormatException, DateFormatException;

    RoomResponse roomToResponse(Room room);

    default RoomListResponse roomListResponseList(List<Room> rooms) {
        RoomListResponse response = new RoomListResponse();
        response.setRoomResponses(rooms.stream().map(this::roomToResponse).collect(Collectors.toList()));
        return response;
    }

}
