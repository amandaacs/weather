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

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CityNotFoundWeatherTest {

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
    void shouldReturn404WhenCityNotFound() throws Exception {
        server.expect(requestTo("https://brasilapi.com.br/api/cptec/v1/cidade/CidadeInexistente"))
                .andRespond(withStatus(NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"Nenhuma cidade localizada\"}"));

        mockMvc.perform(get("/api/v1/clima/CidadeInexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value(true))
                .andExpect(jsonPath("$.codigo").value("CIDADE_NAO_ENCONTRADA"))
                .andExpect(jsonPath("$.mensagem").value("Nenhuma cidade encontrada com o nome informado"))
                .andExpect(jsonPath("$.nome_informado").value("CidadeInexistente"));

        server.verify();
    }

    @Test
    void shouldReturn400WhenCityNameIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/clima/X"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value(true))
                .andExpect(jsonPath("$.codigo").value("NOME_INVALIDO"))
                .andExpect(jsonPath("$.nome_informado").value("X"));
    }
}
