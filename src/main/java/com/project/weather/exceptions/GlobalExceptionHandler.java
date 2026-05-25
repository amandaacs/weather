package com.project.weather.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCityNotFound(CityNotFoundException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("erro", true);
        error.put("codigo", "CIDADE_NAO_ENCONTRADA");
        error.put("mensagem", "Nenhuma cidade encontrada com o nome informado");
        error.put("nome_informado", ex.getCityName());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidCityNameException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCityName(InvalidCityNameException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("erro", true);
        error.put("codigo", "NOME_INVALIDO");
        error.put("mensagem", "O nome da cidade deve conter pelo menos 2 caracteres");
        error.put("nome_informado", ex.getCityName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidUfException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUf(InvalidUfException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("erro", true);
        error.put("codigo", "SIGLA_UF_INVALIDA");
        error.put("mensagem", "A sigla do estado deve conter exatamente 2 letras");
        error.put("sigla_uf_informada", ex.getStateCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UfNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUfNotFound(UfNotFoundException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("erro", true);
        error.put("codigo", "UF_NAO_ENCONTRADA");
        error.put("mensagem", "Estado com a sigla informada não foi encontrado");
        error.put("sigla_uf_informada", ex.getStateCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ExternalServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleExternalServiceUnavailable(
            ExternalServiceUnavailableException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("erro", true);
        error.put("codigo", "SERVICO_EXTERNO_INDISPONIVEL");
        error.put("mensagem",
                "Não foi possível obter dados do serviço externo. Tente novamente em alguns instantes");
        error.put("servico", ex.getServiceName());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}
