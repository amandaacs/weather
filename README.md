# API de Agregação de Dados Climáticos e Geográficos

API REST desenvolvida para a disciplina **Técnicas de Integração de Sistemas (N703)**. A aplicação integra APIs públicas brasileiras (Brasil API — IBGE e CPTEC/INPE) para fornecer informações geográficas e climáticas sobre cidades do Brasil.

O usuário informa apenas o **nome da cidade** ou a **sigla do estado**; a API resolve a localização e consulta os serviços externos automaticamente (sem coordenadas fixas no código).

## Requisitos

- Java 17+
- Maven (ou use o wrapper incluído: `./mvnw`)

## Como executar

```bash
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:3000`.

Página inicial (lista de endpoints):

```
GET http://localhost:3000/
```

## Endpoints

### Health Check

```
GET /api/v1/health
```

Retorna o status da aplicação (`healthy` ou `degraded`) e verifica a disponibilidade dos serviços externos (IBGE e CPTEC).

### Clima por cidade

```
GET /api/v1/clima/{nome_cidade}
```

Exemplos:

```
GET /api/v1/clima/Fortaleza
GET /api/v1/clima/Sao%20Paulo
```

Busca a cidade pelo nome e retorna previsão meteorológica (temperatura mín/máx, condição do tempo, estado).

**Erros tratados:**

| HTTP | Código |
|------|--------|
| 400 | `NOME_INVALIDO` |
| 404 | `CIDADE_NAO_ENCONTRADA` |
| 503 | `SERVICO_EXTERNO_INDISPONIVEL` |

### Cidades por estado

```
GET /api/v1/cidades/{sigla_uf}?limite=10
```

Exemplo:

```
GET /api/v1/cidades/CE?limite=5
```

Lista municípios de um estado via Brasil API (IBGE). O parâmetro `limite` aceita valores de **1 a 100** (padrão: **10**).

**Erros tratados:**

| HTTP | Código |
|------|--------|
| 400 | `SIGLA_UF_INVALIDA` |
| 404 | `UF_NAO_ENCONTRADA` |

## Testes automatizados

```bash
./mvnw test
```

Os testes ficam na pasta `tests/` na raiz do repositório (mínimo exigido pela atividade):

| Arquivo | O que verifica |
|---------|----------------|
| `ValidWeatherTest.java` | Resposta correta para cidade válida |
| `CityNotFoundWeatherTest.java` | Erro 404 e 400 para clima |

## Coleção Postman

Importe o arquivo `docs/postman_collection.json` no [Postman](https://www.postman.com/) para testar todos os endpoints (sucesso e erros) sem digitar as URLs manualmente.

## APIs externas utilizadas

| API | Uso no projeto |
|-----|----------------|
| [Brasil API — IBGE](https://brasilapi.com.br/docs#tag/IBGE) | Listagem de municípios por UF |
| [Brasil API — CPTEC](https://brasilapi.com.br/docs#tag/CPTEC) | Busca de cidade por nome e previsão do tempo |

A previsão é obtida internamente via `/api/cptec/v1/clima/previsao/{id}` após resolver o código da cidade — esse endpoint **não** é exposto na nossa API; o cliente usa apenas `/api/v1/clima/{nome_cidade}`.

## Estrutura do repositório

```
/
├── README.md
├── INTEGRANTES.md
├── src/                         # Código-fonte (Spring Boot)
│   └── main/java/com/project/weather/
│       ├── controllers/         # CityController, WeatherController, HealthController
│       ├── services/            # IbgeService, CptecService, HealthService
│       ├── exceptions/          # Tratamento global de erros
│       └── dto/                 # Mapeamento JSON das APIs externas
├── tests/                       # Testes automatizados
└── docs/
    └── postman_collection.json  # Coleção Postman
```

## Integrantes

Consulte o arquivo [INTEGRANTES.md](INTEGRANTES.md).
