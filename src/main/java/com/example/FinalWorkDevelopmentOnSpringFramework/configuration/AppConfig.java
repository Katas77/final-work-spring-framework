package com.example.FinalWorkDevelopmentOnSpringFramework.configuration;

import com.example.FinalWorkDevelopmentOnSpringFramework.init.ExecuteSqlScript;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {

    @Bean
    CommandLineRunner commandLineRunner(ExecuteSqlScript scriptExecutor) {
        return args -> {
            scriptExecutor.executeSqlFile("data.sql");
        };
    }
}