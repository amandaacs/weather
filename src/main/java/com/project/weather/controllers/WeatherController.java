package com.project.weather.controllers;

import com.project.weather.exceptions.InvalidCityNameException;
import com.project.weather.services.CptecService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/clima")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final CptecService cptecService;

    public WeatherController(CptecService cptecService) {
        this.cptecService = cptecService;
    }

    @GetMapping("/{nome_cidade}")
    public ResponseEntity<Map<String, Object>> getWeather(@PathVariable String nome_cidade) {
        if (nome_cidade == null || nome_cidade.trim().length() < 2) {
            throw new InvalidCityNameException(nome_cidade == null ? "" : nome_cidade);
        }

        return ResponseEntity.ok(cptecService.findWeather(nome_cidade.trim()));
    }
}
