package com.project.weather.exceptions;

public class InvalidCityNameException extends RuntimeException {

    private final String cityName;

    public InvalidCityNameException(String cityName) {
        super("Invalid city name: " + cityName);
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
