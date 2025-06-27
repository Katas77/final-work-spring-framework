package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.CreateUserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<UserListResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(userMapper.userListResponseList(userService.findAll(pageNumber, pageSize)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> findByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.findByUserNameResponse(name));
    }
    @PostMapping("/public")
    public ResponseEntity<String> create(@RequestBody @Valid CreateUserRequest request, @RequestParam("roleType")  RoleType roleType) throws UserAlreadyExistsException {
        return ResponseEntity.ok(userService.create(userMapper.requestToUser(request), roleType));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long userId, @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.update(userMapper.requestToUser(userId, request)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws BusinessLogicException {
        return ResponseEntity.ok(userService.deleteById(id));
    }

    @GetMapping("/public/isPresent/{name}/{email}")
    public ResponseEntity<String> isPresent(@PathVariable String name, @PathVariable String email) throws UserAlreadyExistsException {
        return ResponseEntity.ok(userService.emailAndUserIsPresent(name, email));
    }

}
