package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer;


import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model.BookingEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.model.UserEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceProducer {
    @Value("${topic}")
    private String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        System.out.println("Sending message to topic '" + topicName + "'...");
        kafkaTemplate.send(topicName, message);
    }

    public void sendBookingEvent(BookingEvent bookingEvent) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String eventJson = mapper.writeValueAsString(bookingEvent);
            kafkaTemplate.send(topicName, eventJson);
            log.info("Send bookingEvent: {}", eventJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert account to JSON: {} - don't send", bookingEvent.toString(), e);
        }
    }


    public void sendUserEvent(UserEvent userEvent) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String eventJson =mapper.writeValueAsString(userEvent);
            kafkaTemplate.send(topicName, eventJson);
            log.info("Send userEvent: {}", eventJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert account to JSON: {} - don't send", userEvent.toString(), e);
        }
    }

}
