package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.CreateUserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
@AllArgsConstructor
public class UserMapperAllField implements UserMapper {
    @Override
    public User requestToUser(CreateUserRequest request) {
        if (request == null) {
            return null;
        }
        return User.builder()
                .password(request.getPassword())
                .name(request.getName())
                .email_address(request.getE_mail())
                .build();
    }

    @Override
    public User requestToUser(Long userId, CreateUserRequest request) {
        if (request == null) {
            return null;
        }
        return User.builder()
                .id(userId)
                .password(request.getPassword())
                .name(request.getName())
                .email_address(request.getE_mail())
                .build();
    }

    @Override
    public UserResponse userToResponse(User user) {
        if (user == null) {
            return null;
        }

        List<RoleType> roleTypes = extractRoleTypesFromUser(user);

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .e_mail(user.getEmail_address())
                .roles(roleTypes)
                .build();
    }

    private List<RoleType> extractRoleTypesFromUser(User user) {
        List<RoleType> roleTypes = new ArrayList<>();
        user.getRoles().forEach(role -> roleTypes.add(role.getAuthority()));
        return roleTypes;
    }

}
