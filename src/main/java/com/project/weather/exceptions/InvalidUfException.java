package com.project.weather.exceptions;

public class InvalidUfException extends RuntimeException {

    private final String stateCode;

    public InvalidUfException(String stateCode) {
        super("Invalid state code: " + stateCode);
        this.stateCode = stateCode;
    }

    public String getStateCode() {
        return stateCode;
    }
}
