package com.project.weather.services;

import com.project.weather.dto.CidadeIbgeDTO;
import com.project.weather.exceptions.UfNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class IbgeService {

    private final RestTemplate restTemplate;

    public IbgeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> buscarCidadesPorUf(String uf) {

        String url = "https://brasilapi.com.br/api/ibge/municipios/v1/" + uf;

        try {

            CidadeIbgeDTO[] response = restTemplate.getForObject(url, CidadeIbgeDTO[].class);

            if(response == null) return List.of();

            return Arrays.stream(response)
                    .map(CidadeIbgeDTO::nome)
                    .toList();

        } catch (HttpClientErrorException.NotFound e) {

            throw new UfNotFoundException(uf);

        }

    }

}
