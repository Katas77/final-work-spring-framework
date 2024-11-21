package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface UserService {
    List<User> findAll(int pageNumber, int pageSize);

    ResponseEntity<String> create(User user, RoleType roleType);

    ResponseEntity<String> update(User user);

    ResponseEntity<String> deleteById(Long id);

    ResponseEntity<UserResponse> findByUserNameResponse(String name);

    User findByUserName(String name);

    ResponseEntity<String> emailAndUserIsPresent(String name, String email);

    User findById(Long id);
}
