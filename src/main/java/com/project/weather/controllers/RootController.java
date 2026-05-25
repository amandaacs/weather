package com.project.weather.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> index() {
        Map<String, Object> endpoints = new LinkedHashMap<>();
        endpoints.put("health", "GET /api/v1/health");
        endpoints.put("weather", "GET /api/v1/clima/{city_name}");
        endpoints.put("cities", "GET /api/v1/cidades/{state_code}?limite=10");

        return ResponseEntity.ok(Map.of(
                "name", "Weather API - N703",
                "version", "1.0.0",
                "endpoints", endpoints
        ));
    }
}
