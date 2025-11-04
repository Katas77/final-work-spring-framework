package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer.dto;


public record UserEvent(
        String name,
        String roleType,
        String eMail
) {
}