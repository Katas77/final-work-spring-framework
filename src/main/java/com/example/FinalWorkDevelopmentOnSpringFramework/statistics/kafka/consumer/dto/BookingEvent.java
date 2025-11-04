package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.kafka.consumer.dto;

public record BookingEvent(
        Long id,
        String dateCheckIn,
        String dateCheckOut,
        Long roomId) {
}

