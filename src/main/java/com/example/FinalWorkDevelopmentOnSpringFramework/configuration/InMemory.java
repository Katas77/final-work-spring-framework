package com.example.FinalWorkDevelopmentOnSpringFramework.configuration;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.Role;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.en.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "mr")
public class InMemory {
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails ramesh = (UserDetails) User.builder()
                .name("roma")
                .password(passwordEncoder().encode("roma22"))
                .roles(List.of(Role.from(RoleType.ROLE_USER)))
                .build();
        UserDetails admin = (UserDetails) User.builder()
                .name("admin")
                .password(passwordEncoder().encode("admin"))
                .roles(List.of(Role.from(RoleType.ROLE_USER)))
                .build();
        return new InMemoryUserDetailsManager(ramesh, admin);
    }
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
