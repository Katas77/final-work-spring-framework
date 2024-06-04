
package com.example.FinalWorkDevelopmentOnSpringFramework.controller;


import jakarta.validation.Valid;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.CreateUserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.userListResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/admin/{pageNumber}/{pageSize}")
    public ResponseEntity<userListResponse> findAll(@PathVariable int pageNumber, @PathVariable int pageSize) {
        return ResponseEntity.ok(userMapper.userListResponseList(userService.findAll(pageNumber, pageSize)));
    }


    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> findByName(@PathVariable String name) {
        return ResponseEntity.ok(userMapper.userToResponse(userService.findByUserName(name)));
    }

    @PostMapping("/public")
    public ResponseEntity<String> create(@RequestBody @Valid CreateUserRequest request, @RequestParam RoleType roleType) {
        return userService.create(userMapper.requestToUser(request), roleType);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long userId, @RequestBody CreateUserRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        return userService.update(userMapper.requestToUser(userId, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return userService.deleteById(id);
    }

    @GetMapping("/isPresent/{name}/{email}")
    public ResponseEntity<String> isPresent (@PathVariable String name,@PathVariable String email) {
        return userService.emailAndUserIsPresent(name,email);
    }

}
