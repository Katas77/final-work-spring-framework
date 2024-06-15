package com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {
    private List<UserResponse> userResponses = new ArrayList<>();
}
