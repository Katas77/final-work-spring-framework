package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.CreateRoomRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomUpdateRequest;
import java.io.UTFDataFormatException;
import java.util.List;
import java.util.stream.Collectors;

public interface RoomMapper {

    Room createRoomRequestToRoom(CreateRoomRequest request) throws UTFDataFormatException, BusinessLogicException;

    Room roomUpdateRequestToRoom(RoomUpdateRequest request) throws UTFDataFormatException, BusinessLogicException;

    RoomResponse roomToResponse(Room room);

    default RoomListResponse roomListResponseList(List<Room> rooms) {
        RoomListResponse response = new RoomListResponse();
        response.setRoomResponses(rooms.stream().map(this::roomToResponse).collect(Collectors.toList()));
        return response;
    }

}
