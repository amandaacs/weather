package com.project.weather.exceptions;

public class CityNotFoundException extends RuntimeException {

    public CityNotFoundException(String cityName) {
        super("Nenhuma cidade encontrada com o nome informado: " + cityName);
    }

}
