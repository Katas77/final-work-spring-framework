package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.LogExecutionTime;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.repository.StatisticsRepository;
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
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {
    private final StatisticsRepository repository;

    public List<Statistics> findAll() throws IOException {
        List<Statistics> statisticsList = repository.findAll();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Statistics.class)
                .withColumnSeparator(';')
                .withoutQuoteChar()
                .withHeader();
        ObjectWriter writer = mapper.writer(schema);
        writer.writeValue(new FileWriter("data/statistic2.csv", StandardCharsets.UTF_8), statisticsList);
        return repository.findAll();

    }

    public Statistics save(Statistics statistics) {
        String id = UUID.randomUUID().toString().substring(0, 4);
        statistics.setId(id);
        return repository.save(statistics);
    }
@LogExecutionTime
    public ResponseEntity<Resource> findFileAll() throws IOException {
        List<Statistics> statisticsList = repository.findAll();
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Statistics.class)
                .withColumnSeparator(';')
                .withoutQuoteChar()
                .withHeader();
        ObjectWriter writer = mapper.writer(schema);
        String filePath = Paths.get("C:/Users/krp77/Downloads", "statistic.csv").toAbsolutePath().toString();
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



