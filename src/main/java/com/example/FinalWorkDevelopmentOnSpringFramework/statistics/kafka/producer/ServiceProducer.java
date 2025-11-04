package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.producer;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.Booking;

import com.example.FinalWorkDevelopmentOnSpringFramework.model.user.User;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer.dto.BookingEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer.dto.UserEvent;
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

    @Value("${topics.booking}")
    private String BOOKING_TOPIC;

    @Value("${topics.user}")
    private String USER_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendBookingEvent(Booking booking) {
        ObjectMapper mapper = new ObjectMapper();
        BookingEvent event=new BookingEvent(booking.getId(), booking.getDateCheck_in().toString(),booking.getDateCheck_out().toString(), booking.getRoom().getId());
        try {
            String eventJson = mapper.writeValueAsString(event);
            kafkaTemplate.send(BOOKING_TOPIC, eventJson);
            log.info("Send bookingEvent: {}", eventJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert account to JSON: {} - don't send", booking.toString(), e);
        }
    }


    public void sendUserEvent(User user) {
        UserEvent event=new  UserEvent(user.getName(), user.getRoles().toString(), user.getEmail_address());
        ObjectMapper mapper = new ObjectMapper();
        try {
            String eventJson =mapper.writeValueAsString(event);
            kafkaTemplate.send(USER_TOPIC, eventJson);
            log.info("Send userEvent: {}", eventJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert account to JSON: {} - don't send", user.toString(), e);
        }
    }

}
