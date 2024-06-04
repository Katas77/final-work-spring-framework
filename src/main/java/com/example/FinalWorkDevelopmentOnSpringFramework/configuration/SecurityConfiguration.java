package com.example.FinalWorkDevelopmentOnSpringFramework.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "db")//безопасное хранение паролей за счет интеграции с PasswordEncoder. Вы можете настроить PasswordEncoder реализацию, используемую Spring Security, открыв PasswordEncoderBean .
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.security", name = "type", havingValue = "db")// AuthenticationManager — это основной стратегический интерфейс аутентификации Если участник входной аутентификации действителен и проверен AuthenticationManager#authenticate возвращает экземпляр Authentication с флагом аутентификации , установленным в значение true
    public AuthenticationManager inMemoryAuthenticationManager(HttpSecurity http,
                                                               UserDetailsService inkemoryUserDetailsService) throws Exception {
        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(inkemoryUserDetailsService);
        return authManagerBuilder.build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app. security", name = "type", havingValue = "db")
    public AuthenticationManager databaseAuthenticationManager(HttpSecurity http,
                                                               UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(userDetailsService);
        var authProvider = new DaoAuthenticationProvider(passwordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        authManagerBuilder.authenticationProvider(authProvider);
        return authManagerBuilder.build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {//Spring Security по умолчанию предоставляет ряд фильтров. ы можем зарегистрировать фильтр программно, создав bean-компонент SecurityFilterChain .
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/user/**")
                        .permitAll()
                        .requestMatchers("/api/hotel/**")
                        .permitAll()
                        .requestMatchers("/api/booking/**")
                        .hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager);
        return http.build();
    }


}
