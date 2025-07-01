# Neocamp - Teamcubation
## Projeto Final  
### API de Partidas de Futebol

---

## Apresentação

A aplicação consistirá em criar uma API de partidas de futebol, onde o usuário poderá manusear dados de clubes, de partidas e de estádios, bem como fazer cruzamento de dados dessas entidades.

---

## Requisitos não-funcionais

1. A aplicação deverá usar linguagem de programação Java versão 17 ou superior, com framework Spring Boot e Spring Data.
2. JUnit & Mockito para cobertura de testes (ideal: 80%, mínimo: 50%).
3. A aplicação deverá utilizar MySQL como banco de dados.
4. O código-fonte final deverá estar hospedado no GitHub.  
   * Criar um repositório novo para o projeto final. Usar de forma progressiva e realizar commits por etapas de desenvolvimento.

---

## Requisitos funcionais mínimos

### 1. Cadastrar um clube

- **Método:** `POST`
- **Retorno esperado:** `201 CREATED`
- **Campos mínimos:** Nome do clube, sigla do estado da sede, data de criação, situação (ativo)

**Cenários de exceção:**
  - Dados inválidos: Faltam campos obrigatórios, nome com menos de 2 letras, sigla de estado inexistente no Brasil, data de criação no futuro.  
    - **Resposta:** `400 BAD REQUEST`
  - Conflito de dados: Nome de clube igual de outro clube do mesmo estado.  
    - **Resposta:** `409 CONFLICT`

---

### 2. Editar um clube

- **Método:** `PUT`
- **Retorno esperado:** `200 OK`

**Cenários de exceção:**
  - Dados inválidos: Nome com menos de 2 letras, estado inexistente, data de criação no futuro.  
    - **Resposta:** `400 BAD REQUEST`
  - Data inválida: Data de criação posterior à partida já registrada.
    - **Resposta:** `409 CONFLICT`
  - Conflito de dados: Nome igual ao de outro clube do mesmo estado.  
    - **Resposta:** `409 CONFLICT`
  - Clube não existe:
    - **Resposta:** `404 NOT FOUND`

---

### 3. Inativar um clube (SOFT DELETE)

- **Método:** `DELETE`
- **Retorno esperado:** `204 NO CONTENT`

**Cenários de exceção:**
  - Clube não existe:
    - **Resposta:** `404 NOT FOUND`

---

### 4. Buscar um clube

- **Método:** `GET`
- **Retorno esperado:** `200 OK`

**Cenários de exceção:**
  - Sem resultado:
    - **Resposta:** `404 NOT FOUND`

---

### 5. Listar clubes

- **Método:** `GET`
- **Retorno esperado:** `200 OK`
- Permite filtrar por nome, estado ou situação. Permite paginação e ordenação crescente/decrescente.

**Cenários de exceção:**
  - Sem resultado:  
    - Retornar lista vazia com status `200 OK`.

---

### 6. Cadastrar uma partida

- **Método:** `POST`
- **Retorno esperado:** `201 CREATED`
- **Campos mínimos:** dois clubes, resultado, estádio, data/hora

**Cenários de exceção:**
  - Dados inválidos: Faltam campos, dois clubes iguais, clube ou estádio inexistente, gols negativos, data/hora no futuro.  
    - **Resposta:** `400 BAD REQUEST`
  - Data inválida: Data/hora anterior à criação de um dos clubes envolvidos.
    - **Resposta:** `409 CONFLICT`
  - Clube inativo.
    - **Resposta:** `409 CONFLICT`
  - Partidas com horários próximos (menos de 48h).
    - **Resposta:** `409 CONFLICT`
  - Estádio já possui jogo no mesmo dia.
    - **Resposta:** `409 CONFLICT`

---

### 7. Editar uma partida

- **Método:** `PUT`
- **Retorno esperado:** `200 OK`

**Cenários de exceção:**
  - Dados inválidos: Dois clubes iguais, clube/estádio inexistente, gols negativos, data/hora no futuro.
    - **Resposta:** `400 BAD REQUEST`
  - Data inválida: Data/hora anterior à criação de um dos clubes envolvidos.
    - **Resposta:** `409 CONFLICT`
  - Clube inativo.
    - **Resposta:** `409 CONFLICT`
  - Partidas com horários próximos (menos de 48h).
    - **Resposta:** `409 CONFLICT`
  - Estádio já possui jogo no mesmo dia.
    - **Resposta:** `409 CONFLICT`
  - Partida não existe.
    - **Resposta:** `404 NOT FOUND`

---

### 8. Remover uma partida (HARD DELETE)

- **Método:** `DELETE`
- **Retorno esperado:** `204 NO CONTENT`

**Cenários de exceção:**
  - Partida não existe.
    - **Resposta:** `404 NOT FOUND`

---

### 9. Buscar uma partida

- **Método:** `GET`
- **Retorno esperado:** `200 OK`

**Cenários de exceção:**
  - Sem resultado:  
    - **Resposta:** `404 NOT FOUND`

---

### 10. Listar partidas

- **Método:** `GET`
- **Retorno esperado:** `200 OK`
- Permite filtrar por clube, estádio, paginação e ordenação.

**Cenários de exceção:**
  - Sem resultado:
    - Lista vazia com status `200 OK`

---

### 11. Cadastrar um estádio

- **Método:** `POST`
- **Retorno esperado:** `201 CREATED`
- **Campos mínimos:** nome do estádio

**Cenários de exceção:**
  - Dados inválidos: nome com menos de 3 letras.
    - **Resposta:** `400 BAD REQUEST`
  - Estádio já existe (nome idêntico).
    - **Resposta:** `409 CONFLICT`

---

### 12. Editar um estádio

- **Método:** `PUT`
- **Retorno esperado:** `200 OK`

**Cenários de exceção:**
  - Dados inválidos: nome com menos de 3 letras.
    - **Resposta:** `400 BAD REQUEST`
  - Estádio já existe (nome idêntico).
    - **Resposta:** `409 CONFLICT`
  - Estádio não existe.
    - **Resposta:** `404 NOT FOUND`

---

### 13. Buscar um estádio

- **Método:** `GET`
- **Retorno esperado:** `200 OK`

**Cenários de exceção:**
  - Sem resultado:
    - **Resposta:** `404 NOT FOUND`

---

### 14. Listar estádios

- **Método:** `GET`
- **Retorno esperado:** `200 OK`
- Permite paginação e ordenação pelo nome.

**Cenários de exceção:**
  - Sem resultado:
    - Lista vazia com status `200 OK`

---

## Buscas avançadas

### 1. Retrospecto geral de um clube

- **Método:** `GET`
- **Retorno esperado:** `200 OK`
- Infos: total de vitórias, empates, derrotas, gols feitos e sofridos

**Cenários de exceção:**
  - Sem partida registrada: retrospecto zerado, status `200 OK`
  - Clube não existir: `404 NOT FOUND`

---

### 2. Retrospecto de um clube contra adversários

- **Método:** `GET`
- **Retorno esperado:** `200 OK`
- Infos: total de vitórias, empates, derrotas, gols feitos e sofridos por adversário

**Cenários de exceção:**
  - Sem partida registrada: lista vazia, status `200 OK`
  - Clube não existir: `404 NOT FOUND`

---

### 3. Confrontos diretos

- **Método:** `GET`
- **Retorno esperado:** `200 OK`
- Infos: lista de partidas e retrospecto do confronto

**Cenários de exceção:**
  - Confronto inexistente: lista vazia, retrospecto zerado, status `200 OK`
  - Um dos clubes não existe: `404 NOT FOUND`

---

### 4. Ranking

- **Método:** `GET`
- **Retorno esperado:** `200 OK`
- Rankeamento por:
    - total de pontos (vitória = 3, empate = 1)
    - total de gols
    - total de vitórias
    - total de jogos
- Excluindo clubes sem pontuação/gols/vitórias/jogos

**Cenários de exceção:**
  - Sem resultado para ranking: lista vazia, status `200 OK`

---

## Filtros avançados (extra)

1. **Goleadas**: Filtro de partidas onde diferença no placar é igual ou superior a 3 gols para um dos clubes.
2. **Mandantes e visitantes**: Filtro para partidas em que um clube atuou como mandante ou visitante.
