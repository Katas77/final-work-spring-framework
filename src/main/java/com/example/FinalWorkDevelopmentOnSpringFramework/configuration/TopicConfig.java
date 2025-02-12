package com.example.FinalWorkDevelopmentOnSpringFramework.configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import java.util.Collections;
import java.util.Set;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class TopicConfig {
    private final KafkaAdmin kafkaAdmin;

    @PostConstruct
    public void createTopicsIfNotExist() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            ListTopicsResult topicsResult = adminClient.listTopics();
            Set<String> existingTopics = topicsResult.names().get();
            if (!existingTopics.contains("booking-service")) {
                NewTopic newTopic = new NewTopic("booking-service", 1, (short) 1); // имя топика, количество партиций, фактор репликации
                CreateTopicsResult result = adminClient.createTopics(Collections.singletonList(newTopic));
                result.all().get(); // ожидание завершения создания топика
                log.info("Топик 'booking-service' успешно создан.");
            } else {
                System.out.println( "Топик 'booking-service' уже существует.");
            }
        } catch (Exception e) {
            log.error("Не удалось создать топик 'booking-service'", e);
        }
    }
}
