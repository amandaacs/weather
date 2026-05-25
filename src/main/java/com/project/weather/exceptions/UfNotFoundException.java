package com.project.weather.exceptions;

public class UfNotFoundException extends RuntimeException {

    private final String stateCode;

    public UfNotFoundException(String stateCode) {
        super("State not found: " + stateCode);
        this.stateCode = stateCode;
    }

    public String getStateCode() {
        return stateCode;
    }
}
