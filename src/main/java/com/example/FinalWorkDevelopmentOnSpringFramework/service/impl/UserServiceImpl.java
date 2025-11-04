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
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.dto.UserResponse;
import com.example.FinalWorkDevelopmentOnSpringFramework.web.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll(int pageNumber, int pageSize) {
        PageParams params = normalizePageParams(pageNumber, pageSize);
        return userRepository.findAll(PageRequest.of(params.page(), params.size())).getContent();
    }

    @Override
    public String create(User user, RoleType roleType) {
        checkIfUserExists(user.getName(), user.getEmail_address());
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password must not be null or blank");
        }

        Role role = Role.from(roleType);
        user.setRoles(Collections.singletonList(role));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        role.setUser(user);

        User saved = userRepository.saveAndFlush(user);
        Long id = saved.getId();

        try {
            serviceProducer.sendUserEvent(saved);
        } catch (Exception ex) {
            log.warn("Failed to send user event for userId={} : {}", id, ex.getMessage());
        }

        log.info("Created user id={} name={}", id, saved.getName());
        return MessageFormat.format("User with name {0} saved", saved.getName());
    }

    @Override
    public String update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("User and its id must not be null");
        }

        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("User with ID {0} not found", user.getId())));

        copyNonNullProperties(user, existingUser);
        userRepository.save(existingUser);
        log.info("Updated user id={}", existingUser.getId());
        return MessageFormat.format("User with ID {0} updated", user.getId());
    }

    @Override
    public String deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (!userRepository.existsById(id)) {
            throw new NotFoundException(MessageFormat.format("User with ID {0} not found", id));
        }
        try {
            userRepository.deleteById(id);
            log.info("Deleted user id={}", id);
            return MessageFormat.format("User with ID {0} deleted", id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(MessageFormat.format("User with ID {0} not found", id), e);
        } catch (Exception e) {
            log.error("Failed to delete user id={} : {}", id, e.getMessage());
            throw new BusinessLogicException(MessageFormat.format("Failed to delete user with ID {0}", id), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findByUserNameResponse(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        Optional<User> userOptional = userRepository.findByName(name);
        return userOptional.map(UserMapper::toResponse)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("User with name {0} not found", name)));
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUserName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be null or blank");
        }
        return userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Username not found!"));
    }


    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        MessageFormat.format("User with ID {0} not found", id)));
    }
    @Override
    public String emailAndUserIsPresent(String name, String email) throws UserAlreadyExistsException {
        checkIfUserExists(name, email);
        return "A user with this name and email is not registered.";
    }


    private void checkIfUserExists(String name, String email) throws UserAlreadyExistsException {
        if (name != null && userRepository.findByName(name).isPresent()) {
            throw new UserAlreadyExistsException(
                    MessageFormat.format("User with name {0} already exists", name));
        }
        if (email != null && userRepository.findByEmailAddress(email).isPresent()) {
            throw new UserAlreadyExistsException(
                    MessageFormat.format("E-mail address {0} already exists", email));
        }
    }

    private void copyNonNullProperties(User source, User target) {
        if (source.getName() != null) {
            target.setName(source.getName());
        }
        if (source.getEmail_address() != null) {
            target.setEmail_address(source.getEmail_address());
        }
        if (source.getPassword() != null && !source.getPassword().isBlank()) {
            target.setPassword(passwordEncoder.encode(source.getPassword()));
        }
        if (source.getRoles() != null && !source.getRoles().isEmpty()) {
            target.setRoles(source.getRoles());
            source.getRoles().forEach(r -> r.setUser(target));
        }
    }

    private PageParams normalizePageParams(int pageNumber, int pageSize) {
        int p = pageNumber < 0 ? 0 : pageNumber;
        int s = pageSize <= 0 ? 10 : pageSize;
        return new PageParams(p, s);
    }

    private static class PageParams {
        private final int page;
        private final int size;

        PageParams(int page, int size) {
            this.page = page;
            this.size = size;
        }

        int page() { return page; }
        int size() { return size; }
    }
}