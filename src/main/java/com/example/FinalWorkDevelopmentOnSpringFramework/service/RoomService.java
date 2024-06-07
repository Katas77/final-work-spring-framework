package com.example.FinalWorkDevelopmentOnSpringFramework.service;


import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.Room;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.FilterRoom;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room.RoomResponse;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface RoomService {

    List<Room> findAll(int pageNumber, int pageSize);

    ResponseEntity<RoomResponse> findById(Long id);

    ResponseEntity<String> save(Room room);

    ResponseEntity<String> update(Room room);

    ResponseEntity<String> deleteById(Long id);


    ResponseEntity<RoomListResponse> findFilter(int pageNumber, int pageSize, FilterRoom request);
}
