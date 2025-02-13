package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer;

import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;

import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.StatisticsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {
    private final StatisticsService service;

    @KafkaListener(topics = "${topic}", id = "foo")
    public void listen(String message) throws JsonProcessingException {
        Statistics statistics = Statistics.builder().event(message).build();
        service.save(statistics);
        log.info("Received message: {}", message);
        ObjectMapper mapper = new ObjectMapper();
        String withoutBackslashes = message.replace("\\", "");
        String result = withoutBackslashes.substring(1, withoutBackslashes.length() - 1);
        log.info("Received Message in group " + "  : " + result);
        Map<String, Object> map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });
        log.info("After reading value");
        for (Map.Entry entry : map.entrySet()) {
            log.info(entry.toString());
        }
    }

}
