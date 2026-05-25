package com.project.weather.controllers;

import com.project.weather.exceptions.InvalidUfException;
import com.project.weather.services.IbgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cidades")
@CrossOrigin(origins = "*")
public class CityController {

    private final IbgeService ibgeService;

    public CityController(IbgeService ibgeService) {
        this.ibgeService = ibgeService;
    }

    @GetMapping("/{sigla_uf}")
    public ResponseEntity<Map<String, Object>> getCitiesByState(
            @PathVariable String sigla_uf,
            @RequestParam(defaultValue = "10") int limite) {

        if (sigla_uf == null || sigla_uf.length() != 2 || !sigla_uf.matches("[A-Za-z]{2}")) {
            throw new InvalidUfException(sigla_uf == null ? "" : sigla_uf);
        }

        int limit = Math.max(1, Math.min(limite, 100));
        List<String> allCities = ibgeService.findCitiesByState(sigla_uf.toUpperCase());

        List<Map<String, String>> cities = allCities.stream()
                .limit(limit)
                .map(name -> Map.of("nome", name))
                .toList();

        return ResponseEntity.ok(Map.of(
                "uf", sigla_uf.toUpperCase(),
                "quantidade_retornada", cities.size(),
                "cidades", cities,
                "consultado_em", Instant.now().toString()
        ));
    }
}
