package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer;

import com.example.FinalWorkDevelopmentOnSpringFramework.configuration.kafka.KafkaConstants;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer.dto.BookingEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer.dto.UserEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.StatisticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class Listener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final StatisticsService service;
    @KafkaListener(topics = "${topics.user}",  groupId = KafkaConstants.BOOKING_CONSUMER_GROUP_ID)
    public void listenUser(String rawMessage) throws JsonProcessingException {
        try {
            String cleanJson = rawMessage.startsWith("\"") && rawMessage.endsWith("\"")
                    ? rawMessage.substring(1, rawMessage.length() - 1).replace("\\\"", "\"")
                    : rawMessage;

            ObjectMapper mapper = new ObjectMapper();
            UserEvent event = mapper.readValue(cleanJson, UserEvent.class);

            String timestamp = LocalDateTime.now().format(FORMATTER);
            String message = String.format(
                    "%s — Пользователь с именем: %s, ролью: %s и eMail: %s зарегистрирован",
                    timestamp,
                    event.name(),
                    event.roleType(),
                    event.eMail()
            );

            service.save(Statistics.builder().event(message).build());
            log.info("Сохранена статистика user: {}", message);

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения: {}", rawMessage, e);
            throw new RuntimeException(e);
        }
    }
    @KafkaListener(topics = "${topics.booking}",groupId = KafkaConstants.BOOKING_CONSUMER_GROUP_ID+"2")
    public void listen(String rawMessage) {
        try {
            String cleanJson = rawMessage.startsWith("\"") && rawMessage.endsWith("\"")
                    ? rawMessage.substring(1, rawMessage.length() - 1).replace("\\\"", "\"")
                    : rawMessage;

            ObjectMapper mapper = new ObjectMapper();
            BookingEvent event = mapper.readValue(cleanJson, BookingEvent.class);

            String timestamp = LocalDateTime.now().format(FORMATTER);
            String message = String.format(
                    "Оформил бронирование отелей   %s — Пользователь с id = %d, дата заезда: %s, дата выезда: %s, номер комнаты: %d",
                    timestamp,
                    event.id(),
                    event.dateCheckIn(),
                    event.dateCheckOut(),
                    event.roomId()
            );

            service.save(Statistics.builder().event(message).build());
            log.info("Сохранена статистика booking: {}", message);

        } catch (Exception e) {
            log.error("Ошибка при обработке сообщения: {}", rawMessage, e);
            throw new RuntimeException(e);
        }
    }
}