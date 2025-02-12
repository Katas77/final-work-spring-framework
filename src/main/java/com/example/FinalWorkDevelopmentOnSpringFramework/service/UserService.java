package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.UserAlreadyExistsException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface UserService {
    List<User> findAll(int pageNumber, int pageSize);

    ResponseEntity<String> create(User user, RoleType roleType) throws UserAlreadyExistsException;

    ResponseEntity<String> update(User user);

    ResponseEntity<String> deleteById(Long id) throws BusinessLogicException;

    ResponseEntity<UserResponse> findByUserNameResponse(String name);

    User findByUserName(String name);

    ResponseEntity<String> emailAndUserIsPresent(String name, String email) throws UserAlreadyExistsException;

    User findById(Long id);
}
