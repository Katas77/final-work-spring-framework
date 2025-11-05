package com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service;

import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.repository.StatisticsRepository;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        writer.writeValue(new FileWriter("data/statistic.csv", StandardCharsets.UTF_8), statisticsList);
        return repository.findAll();

    }


    public Statistics save(Statistics statistics) {
        String id = UUID.randomUUID().toString().substring(0, 4);
        statistics.setId(id);
        return repository.save(statistics);
    }

}
