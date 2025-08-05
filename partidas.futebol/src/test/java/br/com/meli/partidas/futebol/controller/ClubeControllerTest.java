package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ClubeResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.service.ClubeService;
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
import static br.com.meli.partidas.futebol.enums.Sigla.SP;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ClubeController.class)
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

        Clube clubeEntity = Conversao.dtoToEntity(clubeRequestDTO);
        clubeEntity.setId(1L);


        when(clubeService.salvarClube(Mockito.argThat(clube ->
                clube.getNome().equals(clubeRequestDTO.getNome()) &&
                        clube.getSigla().equals(clubeRequestDTO.getSigla()) &&
                        clube.getDataCriacao().equals(clubeRequestDTO.getDataCriacao()) &&
                        clube.getAtivo() == clubeRequestDTO.getAtivo())
        )).thenReturn(clubeEntity);

        ClubeResponseDTO clubeResponseDTO = Conversao.entityToDTO(clubeEntity);
        var jsonEsperado = clubeResponseDTOJson.write(clubeResponseDTO).getJson();

        mockMvc.perform(
                        post("/clubes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(clubeRequestDTOJson.write(clubeRequestDTO).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonEsperado));
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

        Clube clubeEditado = Conversao.dtoToEntity(clubeRequestDTO);
        clubeEditado.setId(id);

        Clube clubeAtualizado = new Clube(
                id,
                clubeRequestDTO.getNome(),
                clubeRequestDTO.getSigla(),
                clubeRequestDTO.getDataCriacao(),
                clubeRequestDTO.getAtivo()
        );

        when(clubeService.atualizarClube(Mockito.argThat(clube ->
                clube.getId().equals(id) &&
                        clube.getNome().equals(clubeRequestDTO.getNome()) &&
                        clube.getSigla().equals(clubeRequestDTO.getSigla()) &&
                        clube.getDataCriacao().equals(clubeRequestDTO.getDataCriacao()) &&
                        clube.getAtivo() == clubeRequestDTO.getAtivo()
        ))).thenReturn(clubeAtualizado);

        ClubeResponseDTO clubeResponseDTO = Conversao.entityToDTO(clubeAtualizado);
        var jsonEsperado = clubeResponseDTOJson.write(clubeResponseDTO).getJson();

        mockMvc.perform(
                        put("/clubes/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(clubeRequestDTOJson.write(clubeRequestDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonEsperado));
    }

    @Test
    @DisplayName("Deve retornar status 204 ao inativar um clube com sucesso")
    void testInativarClube() throws Exception {
        Long id = 1L;

        Mockito.doNothing().when(clubeService).inativarClube(id);

        mockMvc.perform(
                        delete("/clubes/{id}", id)
                )
                .andExpect(status().isNoContent());

        Mockito.verify(clubeService, Mockito.times(1)).inativarClube(id);
    }

    @Test
    @DisplayName("Deve retornar status 200 ao buscar um clube por id")
    void testBuscarClubePorId() throws Exception {
        Long id = 1L;
        Clube clube = new Clube(
                id,
                "Nome do Clube",
                SP,
                LocalDate.now(),
                true
        );

        when(clubeService.buscarClubePorId(id)).thenReturn(clube);

        ClubeResponseDTO clubeResponseDTO = Conversao.entityToDTO(clube);
        var jsonEsperado = clubeResponseDTOJson.write(clubeResponseDTO).getJson();

        mockMvc.perform(
                        get("/clubes/{id}", id)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(jsonEsperado));
    }
}