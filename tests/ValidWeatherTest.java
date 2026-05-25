package com.project.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ValidWeatherTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private RestTemplate restTemplate;

    private MockMvc mockMvc;
    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnWeatherForValidCity() throws Exception {
        server.expect(requestTo("https://brasilapi.com.br/api/cptec/v1/cidade/Fortaleza"))
                .andRespond(withSuccess(
                        "[{\"nome\":\"Fortaleza\",\"id\":229,\"estado\":\"CE\",\"regiao\":\"Nordeste\"}]",
                        MediaType.APPLICATION_JSON));

        server.expect(requestTo("https://brasilapi.com.br/api/cptec/v1/clima/previsao/229"))
                .andRespond(withSuccess("""
                        {
                          "cidade": "Fortaleza",
                          "estado": "CE",
                          "atualizado_em": "2026-05-21",
                          "clima": [{
                            "data": "2026-05-22",
                            "condicao": "pn",
                            "condicao_desc": "Parcialmente Nublado",
                            "min": 24,
                            "max": 32,
                            "indice_uv": 0
                          }]
                        }
                        """, MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/v1/clima/Fortaleza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fortaleza"))
                .andExpect(jsonPath("$.estado").value("CE"))
                .andExpect(jsonPath("$.clima.temperatura_min").value(24))
                .andExpect(jsonPath("$.clima.temperatura_max").value(32))
                .andExpect(jsonPath("$.clima.condicao").value("Parcialmente Nublado"))
                .andExpect(jsonPath("$.clima.unidades.temperatura").value("°C"))
                .andExpect(jsonPath("$.consultado_em").exists());

        server.verify();
    }
}
