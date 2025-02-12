package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.CreateUserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;

import java.util.List;
import java.util.stream.Collectors;


public interface UserMapper {
    User requestToUser(CreateUserRequest request);

    User requestToUser(Long userId, CreateUserRequest request);


    UserResponse userToResponse(User user);

    default UserListResponse userListResponseList(List<User> users) {
        UserListResponse response = new UserListResponse();
        response.setUserResponses(users.stream().map(this::userToResponse).collect(Collectors.toList()));
        return response;
    }

}
