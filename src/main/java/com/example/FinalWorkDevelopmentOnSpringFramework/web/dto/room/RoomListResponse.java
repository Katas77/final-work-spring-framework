package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomListResponse {
    private List<RoomResponse> roomResponses = new ArrayList<>();
}
