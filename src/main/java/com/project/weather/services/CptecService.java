package com.project.weather.services;

import com.project.weather.dto.CptecCityDTO;
import com.project.weather.dto.CptecForecastDTO;
import com.project.weather.exceptions.CityNotFoundException;
import com.project.weather.exceptions.ExternalServiceUnavailableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
public class CptecService {

    private static final String BASE_URL = "https://brasilapi.com.br/api/cptec/v1";

    private final RestTemplate restTemplate;

    public CptecService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> findWeather(String cityName) {
        CptecCityDTO city = findCity(cityName);
        CptecForecastDTO forecast = findForecast(city.id());

        if (forecast.clima() == null || forecast.clima().isEmpty()) {
            throw new ExternalServiceUnavailableException("CPTEC", null);
        }

        var weatherDay = forecast.clima().get(0);

        return Map.of(
                "nome", city.nome(),
                "estado", city.estado(),
                "clima", Map.of(
                        "temperatura_min", weatherDay.min(),
                        "temperatura_max", weatherDay.max(),
                        "condicao", weatherDay.condicao_desc(),
                        "unidades", Map.of("temperatura", "°C")
                ),
                "consultado_em", java.time.Instant.now().toString()
        );
    }

    public boolean isAvailable() {
        try {
            restTemplate.getForObject(BASE_URL + "/cidade/Fortaleza", CptecCityDTO[].class);
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }

    private CptecCityDTO findCity(String cityName) {
        URI url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/cidade/{cityName}")
                .buildAndExpand(cityName)
                .encode(StandardCharsets.UTF_8)
                .toUri();

        try {
            CptecCityDTO[] cities = restTemplate.getForObject(url, CptecCityDTO[].class);

            if (cities == null || cities.length == 0) {
                throw new CityNotFoundException(cityName);
            }

            return selectBestMatch(cities, cityName);

        } catch (HttpClientErrorException.NotFound e) {
            throw new CityNotFoundException(cityName);
        } catch (HttpClientErrorException e) {
            throw new ExternalServiceUnavailableException("CPTEC", e);
        } catch (ResourceAccessException e) {
            throw new ExternalServiceUnavailableException("CPTEC", e);
        }
    }

    private CptecForecastDTO findForecast(int cityCode) {
        URI url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/clima/previsao/{cityCode}")
                .buildAndExpand(cityCode)
                .toUri();

        try {
            CptecForecastDTO forecast = restTemplate.getForObject(url, CptecForecastDTO.class);

            if (forecast == null) {
                throw new ExternalServiceUnavailableException("CPTEC", null);
            }

            return forecast;

        } catch (HttpClientErrorException e) {
            throw new ExternalServiceUnavailableException("CPTEC", e);
        } catch (ResourceAccessException e) {
            throw new ExternalServiceUnavailableException("CPTEC", e);
        }
    }

    private CptecCityDTO selectBestMatch(CptecCityDTO[] cities, String searchName) {
        String normalized = normalize(searchName);

        Optional<CptecCityDTO> exact = Arrays.stream(cities)
                .filter(c -> normalize(c.nome()).equals(normalized))
                .findFirst();

        if (exact.isPresent()) {
            return exact.get();
        }

        Optional<CptecCityDTO> partial = Arrays.stream(cities)
                .filter(c -> normalize(c.nome()).startsWith(normalized))
                .findFirst();

        return partial.orElse(cities[0]);
    }

    private String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }
}
