package com.example.FinalWorkDevelopmentOnSpringFramework.service.impl;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.UserAlreadyExistsException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.Role;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.UserRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.UserService;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer.ServiceProducer;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model.UserEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.dto.user.UserResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
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
@Transactional
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
    public String create(User user, RoleType roleType) throws UserAlreadyExistsException {
        checkIfUserExists(user.getName(), user.getEmail_address());
        Role role = Role.from(roleType);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);


        Long id = userRepository.saveAndFlush(user).getId();
        serviceProducer.sendUserEvent(UserEvent.builder()
                .recordingFacts(String.valueOf(LocalDateTime.now()))
                .UserId(id)
                .build());

        return MessageFormat.format("User with name {0} saved", user.getName());
    }

    @Override
    public String update(User user) {
        Optional<User> existedUser = userRepository.findById(user.getId());
        if (existedUser.isPresent()) {
            copyNonNullProperties(user, existedUser.get());
            userRepository.save(existedUser.get());
            return MessageFormat.format("User with ID {0} updated", user.getId());
        } else {
           throw new NotFoundException(MessageFormat.format("User with ID {0} not found", user.getId()));
        }
    }

    @Override
    public String deleteById(Long id) throws BusinessLogicException {
        try {
            userRepository.deleteById(id);
            return MessageFormat.format("User with ID {0} deleted", id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(MessageFormat.format("User with ID {0} not found", id), e);
        }
    }

    @Override
    public UserResponse findByUserNameResponse(String name) {
        Optional<User> userOptional = userRepository.findByName(name);
        return userOptional.map(userMapper::userToResponse)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format("User with ID {0} not name", name)));
    }

    @Override
    public User findByUserName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new NotFoundException("Username not found!"));
    }

    @Override
    public String emailAndUserIsPresent(String name, String email) throws UserAlreadyExistsException {
        checkIfUserExists(name, email);
        return "A user with this name and email is not registered.";
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private void checkIfUserExists(String name, String email) throws UserAlreadyExistsException {
        if (userRepository.findByName(name).isPresent()) {
            throw new UserAlreadyExistsException("User with name " + name + " already exists");
        }
        if (userRepository.findByEmailAddress(email).isPresent()) {
            throw new UserAlreadyExistsException("E-mail address " + email + " already exists");
        }
    }

    private void copyNonNullProperties(User source, User target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getEmail_address() != null) {
            target.setEmail_address(source.getEmail_address());
        }
        if (source.getPassword() != null) {
            target.setPassword(passwordEncoder.encode(source.getPassword()));
        }

    }
}