package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.LogExecutionTime;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.repository.StatisticsRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.StatisticsService;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticController {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy_MM_dd");
    private final StatisticsRepository repository;
    private final StatisticsService service;

    @GetMapping()
    public ResponseEntity<List<Statistics>> getStatistics() throws IOException {
        return ResponseEntity.ok(service.findAll());
    }
    @LogExecutionTime
    @GetMapping(path = "/download")
    public ResponseEntity<Resource> downloadFile() throws Exception {
        List<Statistics> statisticsList = repository.findAll();
        String timestamp = LocalDate.now().format(FORMATTER);
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Statistics.class)
                .withColumnSeparator(';')
                .withoutQuoteChar()
                .withHeader();
        ObjectWriter writer = mapper.writer(schema);
        String filePath = Paths.get("C:/Users/krp77/Downloads", "statistic_"+timestamp+".csv").toAbsolutePath().toString();
        writer.writeValue(new FileWriter(filePath, StandardCharsets.UTF_8), statisticsList);
        File downloadFile = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(downloadFile));
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + downloadFile.getName());
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(downloadFile.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}