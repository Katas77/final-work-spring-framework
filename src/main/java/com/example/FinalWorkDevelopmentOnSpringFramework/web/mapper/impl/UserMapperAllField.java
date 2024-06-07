package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.CreateUserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2024-03-30T17:35:46+0300",
        comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.6.jar, environment: Java 21.0.2 (Oracle Corporation)"
)
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
                .emailAddress(request.getE_mail())
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
                .emailAddress(request.getE_mail())
                .build();
    }

    @Override
    public UserResponse userToResponse(User user) {
        if (user == null) {
            return null;
        }
        List<RoleType> roleTypeList = new ArrayList<>();
        user.getRoles().forEach(rol -> roleTypeList.add(rol.getAuthority()));
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .e_mail(user.getEmailAddress())
                .roles(roleTypeList)
                .build();
    }
}
