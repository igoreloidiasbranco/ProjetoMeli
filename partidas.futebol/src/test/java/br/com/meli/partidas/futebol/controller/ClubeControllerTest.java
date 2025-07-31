package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ClubeResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.service.ClubeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static br.com.meli.partidas.futebol.enums.Sigla.SP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@WebMvcTest(ClubeController.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<ClubeRequestDTO> clubeRequestDTOJson;

    @Autowired
    private JacksonTester<ClubeResponseDTO> clubeResponseDTOJson;

    @Autowired
    private ClubeService clubeService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ClubeService clubeService() {
            return Mockito.mock(ClubeService.class);
        }
    }

    @Test
    @DisplayName("Deve retornar status 200 quando salvar um clube com sucesso")
    void testSalvarClube() throws Exception {
        ClubeRequestDTO clubeRequestDTO = new ClubeRequestDTO();
        clubeRequestDTO.setNome("Nome do Clube");
        clubeRequestDTO.setSigla(SP);
        clubeRequestDTO.setDataCriacao(LocalDate.now());
        clubeRequestDTO.setAtivo(true);

        when(clubeService.salvarClube(any()))
                .thenReturn(
                        new Clube(
                                1L,
                                clubeRequestDTO.getNome(),
                                clubeRequestDTO.getSigla(),
                                clubeRequestDTO.getDataCriacao(),
                                clubeRequestDTO.getAtivo()));

        var resultado = mockMvc.perform(
                        post("/clubes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(clubeRequestDTOJson.write(clubeRequestDTO).getJson()))
                .andReturn().getResponse();

        ClubeResponseDTO clubeResponseDTO = new ClubeResponseDTO(
                1L,
                clubeRequestDTO.getNome(),
                clubeRequestDTO.getSigla(),
                clubeRequestDTO.getDataCriacao(),
                clubeRequestDTO.getAtivo()
        );

        var jsonEsperado = clubeResponseDTOJson.write(clubeResponseDTO).getJson();

        assertThat(HttpStatus.CREATED.value()).isEqualTo(resultado.getStatus());
        assertThat(jsonEsperado).isEqualTo(resultado.getContentAsString());
    }


    @Test
    @DisplayName("Deve retornar status 200 ao atualizar um clube com sucesso")
    void testAtualizarClube() throws Exception {
        Long id = 1L;
        ClubeRequestDTO clubeRequestDTO = new ClubeRequestDTO();
        clubeRequestDTO.setNome("Novo Nome");
        clubeRequestDTO.setSigla(SP);
        clubeRequestDTO.setDataCriacao(LocalDate.now());
        clubeRequestDTO.setAtivo(true);

        Clube clubeAtualizado = new Clube(
                id,
                clubeRequestDTO.getNome(),
                clubeRequestDTO.getSigla(),
                clubeRequestDTO.getDataCriacao(),
                clubeRequestDTO.getAtivo()
        );

        when(clubeService.atualizarClube(any()))
                .thenReturn(clubeAtualizado);

        var resultado = mockMvc.perform(
                        put("/clubes/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(clubeRequestDTOJson.write(clubeRequestDTO).getJson()))
                .andReturn().getResponse();

        ClubeResponseDTO clubeResponseDTO = new ClubeResponseDTO(
                id,
                clubeRequestDTO.getNome(),
                clubeRequestDTO.getSigla(),
                clubeRequestDTO.getDataCriacao(),
                clubeRequestDTO.getAtivo()
        );

        var jsonEsperado = clubeResponseDTOJson.write(clubeResponseDTO).getJson();

        assertThat(HttpStatus.OK.value()).isEqualTo(resultado.getStatus());
        assertThat(jsonEsperado).isEqualTo(resultado.getContentAsString());
    }
}