package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.service;



import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.KafkaMessagingTemplate;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.order.OrderBookingEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.order.OrderUserEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.model.BookingEvent;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.template.model.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;



@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceProducer {

    private final KafkaMessagingTemplate kafkaMessagingService;
    private final ModelMapper modelMapper;

    public void sendBookingEvent(BookingEvent bookingEvent) {
        kafkaMessagingService.sendOrderBooking(modelMapper.map(bookingEvent, OrderBookingEvent.class));
        log.info("Send order from producer {}", bookingEvent);

    }
    public void sendUserEvent(UserEvent userEvent) {
        kafkaMessagingService.sendOrderUser(modelMapper.map(userEvent, OrderUserEvent.class));
        log.info("Send order from producer {}", userEvent);

    }
}