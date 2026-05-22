package com.project.weather.exceptions;

public class InvalidUfException extends RuntimeException{

    private final String invalidUf;

    public InvalidUfException(String invalidUf) {
        super("A sigla do estado deve conter exatamente 2 letras");
        this.invalidUf = invalidUf;
    }

    public String getInvalidUf() {
        return invalidUf;
    }

}
