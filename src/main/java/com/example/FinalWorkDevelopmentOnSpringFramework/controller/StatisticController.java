package com.example.FinalWorkDevelopmentOnSpringFramework.controller;

import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.entety.Statistics;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.StatisticsService;
import com.example.FinalWorkDevelopmentOnSpringFramework.statistics.service.dto.FileData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import java.io.IOException;
import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

        @GetMapping(path = "/download")
        public ResponseEntity<Resource> downloadFile() {
            try {
                FileData fileData = service.createStatisticsCsv();
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.filename() + "\"");
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");
                return ResponseEntity.ok()
                        .headers(headers)
                        .contentLength(fileData.contentLength())
                        .contentType(fileData.mediaType())
                        .body(fileData.resource());
            } catch (IOException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Не удалось сформировать CSV-файл", ex);
            }
        }

}

