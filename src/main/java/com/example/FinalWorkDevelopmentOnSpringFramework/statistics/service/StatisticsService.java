package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.aop.LogExecutionTime;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.repository.StatisticsRepository;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.dto.FileData;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
    public FileData createStatisticsCsv() throws IOException {
        List<Statistics> statisticsList = repository.findAll();

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Statistics.class)
                .withColumnSeparator(';')
                .withoutQuoteChar()
                .withHeader();

        Path tempFile = Files.createTempFile("statistic-", ".csv");
        // Чтобы файл удалился при завершении JVM (опционально)
        tempFile.toFile().deleteOnExit();

        try (Writer writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING)) {
            mapper.writer(schema).writeValue(writer, statisticsList);
        }

        Resource resource = new UrlResource(tempFile.toUri());
        long size = Files.size(tempFile);

        return new FileData(resource, tempFile.getFileName().toString(), size, MediaType.APPLICATION_OCTET_STREAM);
    }



}



