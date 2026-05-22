package com.project.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCityNotFound(
            CityNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("erro", true);
        error.put("codigo", "CIDADE_NAO_ENCONTRADA");
        error.put("mensagem", ex.getMessage());
        error.put("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(InvalidCityNameException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCityName(
            InvalidCityNameException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("erro", true);
        error.put("codigo", "NOME_INVALIDO");
        error.put("mensagem", ex.getMessage());
        error.put("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(InvalidUfException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUf(
            InvalidUfException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("erro", true);
        error.put("codigo", "SIGLA_UF_INVALIDA");
        error.put("mensagem", ex.getMessage());
        error.put("timestamp", Instant.now());
        error.put("sigla_uf_informada", ex.getInvalidUf());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(UfNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUfNotFound(
            UfNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("erro", true);
        error.put("codigo", "UF_NAO_ENCONTRADA");
        error.put("mensagem", ex.getMessage());
        error.put("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(
            Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("erro", true);
        error.put("codigo", "ERRO_INTERNO");
        error.put("mensagem", "Erro interno do servidor");
        error.put("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }



}
