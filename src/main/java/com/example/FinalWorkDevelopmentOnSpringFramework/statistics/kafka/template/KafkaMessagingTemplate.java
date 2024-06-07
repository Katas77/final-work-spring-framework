package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template;


import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.order.OrderBookingEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.order.OrderUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessagingTemplate {

    @Value("${topic.status-order}")
    private String sendClientTopic;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderBooking(OrderBookingEvent orderEvent) {
        kafkaTemplate.send(sendClientTopic, orderEvent);

    }

    public void sendOrderUser(OrderUserEvent orderEvent) {
        kafkaTemplate.send(sendClientTopic, orderEvent);

    }
}
