package com.project.weather.exceptions;

public class UfNotFoundException extends RuntimeException {

    public UfNotFoundException(String uf) {
        super("Estado com a sigla informada não foi encontrado: " + uf);
    }

}
