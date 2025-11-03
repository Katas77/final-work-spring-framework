package com.example.FinalWorkDevelopmentOnSpringFramework.configuration;

import com.example.FinalWorkDevelopmentOnSpringFramework.init.ExecuteSqlScript;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    @ConditionalOnProperty(name = "app.init.sql.enabled", havingValue = "true", matchIfMissing = false)
    CommandLineRunner commandLineRunner(ExecuteSqlScript scriptExecutor) {
        return args -> {
            scriptExecutor.executeSqlFile("data.sql");
        };
    }
}