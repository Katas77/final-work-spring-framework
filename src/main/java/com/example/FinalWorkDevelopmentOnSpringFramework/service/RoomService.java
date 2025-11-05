package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.room.dto.FilterRoom;


import java.util.List;

public interface RoomService {
    List<Room> findAll(int pageNumber, int pageSize);

    Room findById(Long id);

    String save(Room room);

    String update(Room room);

    String deleteById(Long id);

    List<Room> findFilter(int pageNumber, int pageSize, FilterRoom request);
}
