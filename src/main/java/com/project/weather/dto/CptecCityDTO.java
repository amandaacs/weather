package com.project.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CptecCityDTO(String nome, String estado, String regiao, int id) {
}
