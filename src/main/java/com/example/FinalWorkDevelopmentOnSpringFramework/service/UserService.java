package com.example.FinalWorkDevelopmentOnSpringFramework.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.UserAlreadyExistsException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    List<User> findAll(int pageNumber, int pageSize);

    String create(User user, RoleType roleType) throws UserAlreadyExistsException;

    String update(User user);

    String deleteById(Long id) throws BusinessLogicException;

    UserResponse findByUserNameResponse(String name);

    User findByUserName(String name);

    String emailAndUserIsPresent(String name, String email) throws UserAlreadyExistsException;

    User findById(Long id);
}
