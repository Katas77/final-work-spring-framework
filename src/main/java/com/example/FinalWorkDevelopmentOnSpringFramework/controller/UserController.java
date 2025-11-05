package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.CustomValid;
import com.example.FinalWorkDevelopmentOnSpringFramework.security.AppUserPrincipal;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.mapper.UserMapper;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserRequest;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(UserMapper.toResponseList(userService.findAll(page, size)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @GetMapping("/{name}")
    public ResponseEntity<UserResponse> findByName(@PathVariable String name) {
        return ResponseEntity.ok(userService.findByUserNameResponse(name));
    }

    @PostMapping("/public")
    @CustomValid
    public ResponseEntity<String> create(@RequestBody UserRequest request,
                                         @RequestParam("roleType") RoleType roleType) {
        return ResponseEntity.created(URI.create("/api/users")).body(userService.create(UserMapper.toEntity(request), roleType));
    }
    @CustomValid
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<String> update(@AuthenticationPrincipal AppUserPrincipal userDetails,
                                         @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.update(UserMapper.toEntity(userDetails.getID(), request)));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public/isPresent")
    public ResponseEntity<String> isPresent(@RequestParam String name, @RequestParam String email) {
        return ResponseEntity.ok(userService.emailAndUserIsPresent(name, email));
    }
}
