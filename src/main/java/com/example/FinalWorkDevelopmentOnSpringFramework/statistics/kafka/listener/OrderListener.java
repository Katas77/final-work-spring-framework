package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.listener;

import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {
    private final StatisticsService service;

    @KafkaListener(id = "foo", topics = "${topic.status-order}")
    public void receive(@Payload String data) {
        Statistics statistics = Statistics.builder().event(data).build();
        service.save(statistics);
        log.info("Received message: {}", data);

    }

}
