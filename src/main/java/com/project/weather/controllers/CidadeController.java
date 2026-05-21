package com.project.weather.controllers;

import com.project.weather.services.IbgeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cidades")
@CrossOrigin(origins = "*")
public class CidadeController {

    private final IbgeService ibgeService;

    public CidadeController(IbgeService ibgeService) {
        this.ibgeService = ibgeService;
    }

    @GetMapping("/{sigla_uf}")
    public ResponseEntity<?> getCidades(@PathVariable String sigla_uf,
                                        @RequestParam(defaultValue = "10") int limite) {

        if(sigla_uf.length() != 2){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "erro", true,
                    "codigo", "SIGLA_UF_INVALIDA",
                    "mensagem", "A sigla do estado deve conter exatamente 2 letras",
                    "uf_informado", sigla_uf
            ));
        }

        try {

            List<String> todasCidades = ibgeService.buscarCidadesPorUf(sigla_uf.toUpperCase());

            int limitefinal = Math.min(limite, 100);
            List<Map<String, String>> cidadesFormat = todasCidades.stream()
                    .limit(limitefinal)
                    .map(nome -> Map.of("nome", nome))
                    .toList();

            return ResponseEntity.ok(Map.of(
                    "uf", sigla_uf.toUpperCase(),
                    "quantidade_retornada", cidadesFormat.size(),
                    "cidades", cidadesFormat,
                    "consultado_em", Instant.now().toString()
            ));

        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "erro", true,
                    "codigo", "UF_NAO_ENCONTRADO",
                    "mensagem", "nao foi encontrado estado com o UF informado",
                    "uf_informado", sigla_uf
            ));

        }

    }
}
