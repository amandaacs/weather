package com.project.weather.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class HealthService {

    private static final String IBGE_URL = "https://brasilapi.com.br/api/ibge/uf/v1";

    private final RestTemplate restTemplate;
    private final CptecService cptecService;

    public HealthService(RestTemplate restTemplate, CptecService cptecService) {
        this.restTemplate = restTemplate;
        this.cptecService = cptecService;
    }

    public boolean isIbgeAvailable() {
        try {
            restTemplate.getForObject(IBGE_URL, Object[].class);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }

    public boolean isCptecAvailable() {
        return cptecService.isAvailable();
    }

    public boolean areAllServicesAvailable() {
        return isIbgeAvailable() && isCptecAvailable();
    }
}
