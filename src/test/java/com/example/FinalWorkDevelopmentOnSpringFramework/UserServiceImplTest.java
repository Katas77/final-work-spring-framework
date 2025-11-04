package com.example.FinalWorkDevelopmentOnSpringFramework;

import com.example.FinalWorkDevelopmentOnSpringFramework.exception.BusinessLogicException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.NotFoundException;
import com.example.FinalWorkDevelopmentOnSpringFramework.exception.UserAlreadyExistsException;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.Role;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.repository.UserRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.service.impl.UserServiceImpl;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer.ServiceProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ServiceProducer serviceProducer;

    @InjectMocks
    private UserServiceImpl userService;

    private User u1;

    @BeforeEach
    void setUp() {
        u1 = User.builder()
                .id(1L)
                .name("john")
                .email_address("john@example.com")
                .password("secret")
                .roles(Collections.singletonList(Role.from(RoleType.ROLE_USER)))
                .build();
    }

    @Test
    void findAll_normalizesPageParams() {
        when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(asList(u1)));

        List<User> res = userService.findAll(-1, 0);
        assertThat(res).containsExactly(u1);
        verify(userRepository).findAll(PageRequest.of(0, 10));
    }

    @Test
    void create_success_and_producerExceptionDoesntFail() throws UserAlreadyExistsException {
        User input = User.builder().name("alice").email_address("a@e.com").password("pwd").build();
        when(userRepository.findByName("alice")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("a@e.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pwd")).thenReturn("encoded");
        when(userRepository.saveAndFlush(any())).thenAnswer(inv -> {
            User arg = inv.getArgument(0);
            arg.setId(2L);
            return arg;
        });
        doThrow(new RuntimeException("kafka fail")).when(serviceProducer).sendUserEvent(any(User.class));

        String res = userService.create(input, RoleType.ROLE_USER);
        assertThat(res).contains("alice");

        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).saveAndFlush(cap.capture());
        User saved = cap.getValue();
        assertThat(saved.getPassword()).isEqualTo("encoded");
        assertThat(saved.getRoles()).isNotEmpty();
        verify(serviceProducer).sendUserEvent(any(User.class));
    }

    @Test
    void create_whenNameExists_throws() {
        User input = User.builder().name("john").email_address("x@x.com").password("p").build();
        when(userRepository.findByName("john")).thenReturn(Optional.of(u1));

        assertThatThrownBy(() -> userService.create(input, RoleType.ROLE_USER)).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void create_whenEmailExists_throws() {
        User input = User.builder().name("new").email_address("john@example.com").password("p").build();
        when(userRepository.findByName("new")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("john@example.com")).thenReturn(Optional.of(u1));

        assertThatThrownBy(() -> userService.create(input, RoleType.ROLE_USER)).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void create_nullPassword_throws() {
        User input = User.builder().name("n").email_address("n@e").password(null).build();
        when(userRepository.findByName("n")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("n@e")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.create(input, RoleType.ROLE_USER)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update_copiesNonNullProperties() {
        User patch = User.builder().id(1L).name("johnny").password("newp").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(u1));
        when(passwordEncoder.encode("newp")).thenReturn("encNewP");
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String res = userService.update(patch);
        assertThat(res).contains("updated");

        ArgumentCaptor<User> cap = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(cap.capture());
        User saved = cap.getValue();
        assertThat(saved.getName()).isEqualTo("johnny");
        assertThat(saved.getPassword()).isEqualTo("encNewP");
    }

    @Test
    void deleteById_notFound_throws() {
        when(userRepository.existsById(5L)).thenReturn(false);
        assertThatThrownBy(() -> userService.deleteById(5L)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteById_whenRepositoryThrows_businessLogicException() {
        when(userRepository.existsById(3L)).thenReturn(true);
        doThrow(new RuntimeException("db error")).when(userRepository).deleteById(3L);

        assertThatThrownBy(() -> userService.deleteById(3L)).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    void findByUserName_found_and_notFound() {
        when(userRepository.findByName("john")).thenReturn(Optional.of(u1));
        User found = userService.findByUserName("john");
        assertThat(found).isEqualTo(u1);

        when(userRepository.findByName("nope")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findByUserName("nope")).isInstanceOf(NotFoundException.class);
    }

    @Test
    void findByUserNameResponse_found_and_notFound() {
        when(userRepository.findByName("john")).thenReturn(Optional.of(u1));
        assertThat(userService.findByUserNameResponse("john")).isNotNull();

        when(userRepository.findByName("nope")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findByUserNameResponse("nope")).isInstanceOf(NotFoundException.class);
    }

    @Test
    void emailAndUserIsPresent_noConflict_returnsMessage() throws UserAlreadyExistsException {
        when(userRepository.findByName("abc")).thenReturn(Optional.empty());
        when(userRepository.findByEmailAddress("abc@e")).thenReturn(Optional.empty());

        String res = userService.emailAndUserIsPresent("abc", "abc@e");
        assertThat(res).contains("not registered");
    }
}
