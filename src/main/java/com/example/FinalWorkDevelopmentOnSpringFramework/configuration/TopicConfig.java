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
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class TopicConfig {

    private final KafkaAdmin kafkaAdmin;
    @Value("${topics.booking}")
    private String BOOKING_TOPIC;

    @Value("${topics.user}")
    private String USER_TOPIC;


    @PostConstruct
    public void createTopicsIfNotExist() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            // Получаем список существующих топиков
            ListTopicsResult topicsResult = adminClient.listTopics();
            Set<String> existingTopics = topicsResult.names().get();

            // Определяем топики, которые нужно создать
            List<NewTopic> topicsToCreate = Stream.of(
                            new NewTopic(BOOKING_TOPIC, 1, (short) 1),
                            new NewTopic(USER_TOPIC, 1, (short) 1)
                    )
                    .filter(topic -> !existingTopics.contains(topic.name()))
                    .collect(Collectors.toList());

            if (!topicsToCreate.isEmpty()) {
                CreateTopicsResult result = adminClient.createTopics(topicsToCreate);
                result.all().get();
                log.info("Созданы Kafka-топики: {}",
                        topicsToCreate.stream().map(NewTopic::name).collect(Collectors.toList()));
            } else {
                log.info("Все необходимые Kafka-топики уже существуют.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Прервано создание Kafka-топиков", e);
        } catch (ExecutionException e) {
            log.error("Ошибка при получении списка существующих топиков", e);
        } catch (Exception e) {
            log.error("Не удалось создать Kafka-топики", e);
        }
    }
}