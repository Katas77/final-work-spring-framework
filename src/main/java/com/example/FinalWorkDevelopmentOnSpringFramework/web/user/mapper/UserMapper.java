package com.example.FinalWorkDevelopmentOnSpringFramework.web.user.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.Role;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserResponse;

import java.util.List;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        return User.builder()
                .name(request.name())
                .email_address(request.eMail())
                .password(request.password())
                .build();
    }
    public static User toEntity(Long id,UserRequest request) {
        return User.builder()
                .id(id)
                .name(request.name())
                .email_address(request.eMail())
                .password(request.password())
                .build();
    }


    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        List<String> roles = user.getRoles().stream()
                .map(Role::toString)
                .toList();
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail_address(),
                roles
        );
    }

    public static List<UserResponse> toResponseList(List<User> users) {
        if (users == null) {
            return List.of();
        }
        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }
}