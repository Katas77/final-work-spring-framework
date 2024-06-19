package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.Role;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.modelEntity.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.UserRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.service.ServiceProducer;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.model.UserEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.utils.BeanUtils;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServiceProducer serviceProducer;
    private final UserMapper userMapper;

    @Override
    public List<User> findAll(int pageNumber, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }
    @Override
    public ResponseEntity<String> create(User user, RoleType roleType) {
        if (userRepository.findByName(user.getName()).isPresent())
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("User with name " + user.getName() + " already exists ");
        if (userRepository.findByEmailAddress(user.getEmailAddress()).isPresent())
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(" E-mail address " + user.getEmailAddress() + " already exists ");
        Role role = Role.from(roleType);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);
        Long id = userRepository.saveAndFlush(user).getId();
        serviceProducer.sendUserEvent(UserEvent.builder().recordingFacts(LocalDateTime.now()).UserId(id).build());
        return ResponseEntity.ok(MessageFormat.format("User with name   {0} save", user.getName()));
    }
    @Override
    public ResponseEntity<String> update(User user) {
        Optional<User> existedUser = userRepository.findById(user.getId());
        if (existedUser.isPresent()) {
            BeanUtils.copyNonNullProperties(user, existedUser.get());
            userRepository.save(existedUser.get());
            return ResponseEntity.ok(MessageFormat.format("User with ID {0} updated", user.getId()));
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("User with ID {0} not found", user.getId()));
        }
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageFormat.format("User with ID {0} not found", id));
        } else {
            userRepository.deleteById(id);
            return ResponseEntity.ok(MessageFormat.format("User with ID {0}  deleted", id));
        }
    }

    @Override
    public ResponseEntity<UserResponse> findByUserNameResponse(String name) {
        Optional<User> userOptional = userRepository.findByName(name);
        return userOptional.map(user -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userMapper.userToResponse(user))).
                orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(null));
    }

    @Override
    public User findByUserName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new RuntimeException("Username not found!"));
    }

    @Override
    public ResponseEntity<String> emailAndUserIsPresent(String name, String email) {
        if (userRepository.findByName(name).isPresent())
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body("User with name " + name + " already exists ");
        if (userRepository.findByEmailAddress(email).isPresent())
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(" E-mail address " + email + " already exists ");
        return ResponseEntity.ok("A user with this name and email is not registered.");
    }

    @Override
    public User findById(Long id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        } else {
            log.info(MessageFormat.format("User ID {0} not found", id));
            return null;
        }
    }

}
