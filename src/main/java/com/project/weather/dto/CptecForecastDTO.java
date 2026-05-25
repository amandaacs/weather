package com.project.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CptecForecastDTO(
        String cidade,
        String estado,
        String atualizado_em,
        List<WeatherDayDTO> clima
) {
}
