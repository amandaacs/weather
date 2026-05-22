package com.project.weather.exceptions;

public class InvalidCityNameException extends RuntimeException{

    public InvalidCityNameException() {
        super("O nome da cidade deve conter pelo menos 2 caracteres");
    }

}
