package com.project.weather.exceptions;

public class CityNotFoundException extends RuntimeException {

    private final String cityName;

    public CityNotFoundException(String cityName) {
        super("City not found: " + cityName);
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
