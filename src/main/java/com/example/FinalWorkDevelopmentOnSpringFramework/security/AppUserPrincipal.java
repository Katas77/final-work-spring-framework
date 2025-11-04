package com.example.FinalWorkDevelopmentOnSpringFramework.security;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.Role;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Этот класс позволяет использовать нашу собственную сущность User (например, из БД) внутри Spring Security.
 * ЗБез этого Spring Security не знал бы, как получить логин, пароль и права доступа.
 */

@RequiredArgsConstructor
public class AppUserPrincipal implements UserDetails {
    private final User user;

    /**
     * Возвращает список прав (ролей) пользователя.
     * Каждая роль преобразуется в GrantedAuthority — стандартный тип Spring Security.
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(Role::toAuthority).toList();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    /**
     * Аккаунт не просрочен?
     * Здесь всегда true — аккаунт бессрочный.
     */

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Аккаунт не заблокирован?
     * Здесь всегда true — блокировки нет.
     */

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Учётные данные (пароль) не просрочены?
     * Здесь всегда true — не требуется менять пароль.
     */

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Пользователь активен?
     * Здесь всегда true — все пользователи считаются включёнными.
     */

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getID() {
        return user.getId();
    }
}
