package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.PartidaResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.enums.Sigla;
import br.com.meli.partidas.futebol.service.PartidaService;
import br.com.meli.partidas.futebol.utils.Conversao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartidaController.class)
@AutoConfigureJsonTesters
class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<PartidaRequestDTO> partidaRequestDTOJson;

    @Autowired
    private JacksonTester<PartidaResponseDTO> partidaResponseDTOJson;

    @Autowired
    private PartidaService partidaService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public PartidaService partidaService() {
            return Mockito.mock(PartidaService.class);
        }
    }


    @Test
    @DisplayName("Deve retornar status 200 quando salvar uma partida com sucesso")
    void testSalvarPartida() throws Exception {

        PartidaRequestDTO partidaRequestDTO = new PartidaRequestDTO();
        partidaRequestDTO.setIdClubeMandante(1L);
        partidaRequestDTO.setIdClubeVisitante(2L);
        partidaRequestDTO.setIdEstadio(3L);
        partidaRequestDTO.setGolsMandante(2);
        partidaRequestDTO.setGolsVisitante(1);
        partidaRequestDTO.setDataHoraPartida(LocalDateTime.of(2024, 6, 1, 16, 0));

        Clube clubeMandante = new Clube(1L, "Clube Mandante", Sigla.SP, LocalDate.of(2000, 1, 1), true);
        Clube clubeVisitante = new Clube(2L, "Clube Visitante", Sigla.RJ, LocalDate.of(2000, 1, 1), true);
        Estadio estadio = new Estadio(3L, "Estádio X", Sigla.SP, new ArrayList<>());

        Partida partida = new Partida();
        partida.setId(10L);
        partida.setIdClubeMandante(clubeMandante);
        partida.setIdClubeVisitante(clubeVisitante);
        partida.setIdEstadio(estadio);
        partida.setGolsMandante(2);
        partida.setGolsVisitante(1);
        partida.setResultado("Clube Mandante 2 x 1 Clube Visitante");
        partida.setDataHoraPartida(partidaRequestDTO.getDataHoraPartida());

        PartidaResponseDTO partidaResponseDTO = Conversao.entityToDTO(partida);


        when(partidaService.validarPartida(Mockito.refEq(partidaRequestDTO), Mockito.isNull())).thenReturn(partida);
        when(partidaService.salvarPartida(Mockito.refEq(partida))).thenReturn(partida);


        mockMvc.perform(post("/partidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partidaRequestDTOJson.write(partidaRequestDTO).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(partidaResponseDTOJson.write(partidaResponseDTO).getJson()));
    }
}