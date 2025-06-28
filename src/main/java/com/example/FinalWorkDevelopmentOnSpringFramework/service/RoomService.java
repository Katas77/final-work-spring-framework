package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.FilterRoom;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomResponse;

import java.util.List;

public interface RoomService {
    List<Room> findAll(int pageNumber, int pageSize);

    RoomResponse findById(Long id);

    String save(Room room);

    String update(Room room);

    String deleteById(Long id);

    RoomListResponse findFilter(int pageNumber, int pageSize, FilterRoom request);
}
