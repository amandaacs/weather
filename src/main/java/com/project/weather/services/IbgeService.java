package com.project.weather.services;

import com.project.weather.dto.IbgeCityDTO;
import com.project.weather.exceptions.UfNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class IbgeService {

    private static final String BASE_URL = "https://brasilapi.com.br/api/ibge/municipios/v1/";

    private final RestTemplate restTemplate;

    public IbgeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> findCitiesByState(String stateCode) {
        String url = BASE_URL + stateCode;

        try {
            IbgeCityDTO[] response = restTemplate.getForObject(url, IbgeCityDTO[].class);

            if (response == null || response.length == 0) {
                throw new UfNotFoundException(stateCode);
            }

            return Arrays.stream(response)
                    .map(IbgeCityDTO::nome)
                    .sorted(Comparator.naturalOrder())
                    .toList();

        } catch (HttpClientErrorException.NotFound e) {
            throw new UfNotFoundException(stateCode);
        }
    }
}
