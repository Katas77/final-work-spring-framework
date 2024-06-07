package com.example.FinalWorkDevelopmentOnSpringFramework.controller;


import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;


@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticController {
    private final StatisticsService service;

    @GetMapping()
    public ResponseEntity<List<Statistics>> getStatistics() throws IOException {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping(path = "/data")
    public ResponseEntity<Resource> downloadFile() throws Exception {
        return service.findFileAll();
    }

}