package com.project.weather.controllers;

import com.project.weather.services.HealthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> body = new LinkedHashMap<>();

        if (healthService.areAllServicesAvailable()) {
            body.put("status", "healthy");
        } else {
            body.put("status", "degraded");
            body.put("motivo", "Serviço externo indisponível");
        }

        body.put("versao", "1.0.0");
        body.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(body);
    }
}
