package com.project.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WeatherDayDTO(
        String data,
        String condicao,
        String condicao_desc,
        int min,
        int max,
        int indice_uv
) {
}
