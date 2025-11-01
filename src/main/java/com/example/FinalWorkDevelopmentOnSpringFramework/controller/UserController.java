package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.UserAlreadyExistsException;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.mapper.UserMapper;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;


    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public List<UserResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return UserMapper.toResponseList(userService.findAll(pageNumber, pageSize));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> findByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.findByUserNameResponse(name));
    }
    @PostMapping("/public")
    public String create(@RequestBody UserRequest request, @RequestParam("roleType")  RoleType roleType) throws UserAlreadyExistsException {
        return userService.create(UserMapper.toEntity(request), roleType);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public String update(@PathVariable("id") Long userId, @RequestBody UserRequest request) {
        return userService.update(UserMapper.toEntity(userId, request));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws BusinessLogicException {
        return userService.deleteById(id);
    }

    @GetMapping("/public/isPresent/{name}/{email}")
    public String isPresent(@PathVariable String name, @PathVariable String email) throws UserAlreadyExistsException {
        return userService.emailAndUserIsPresent(name, email);
    }

}
