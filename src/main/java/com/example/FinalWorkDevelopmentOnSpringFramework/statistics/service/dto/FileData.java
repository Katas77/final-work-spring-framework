package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.dto;



import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record FileData(
        Resource resource,
        String filename,
        long contentLength,
        MediaType mediaType
) {}
