package com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.CreateUserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.userListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {RoomMapper.class})
public interface UserMapper {
    User requestToUser(CreateUserRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, CreateUserRequest request);
    @Mapping(target = "roles", ignore = true)
    UserResponse userToResponse(User user);

    default userListResponse userListResponseList(List<User> users) {
        userListResponse response = new userListResponse();
        response.setUserResponses(users.stream().map(this::userToResponse).collect(Collectors.toList()));
        return response;
    }

}
