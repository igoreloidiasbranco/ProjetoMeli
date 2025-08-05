package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.EstadioRequestDTO;
import br.com.meli.partidas.futebol.dto.response.EstadioResponseDTO;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.enums.Sigla;
import br.com.meli.partidas.futebol.service.EstadioService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadioController.class)
@AutoConfigureJsonTesters
class EstadioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<EstadioRequestDTO> estadioRequestDTOJson;

    @Autowired
    private JacksonTester<EstadioResponseDTO> estadioResponseDTOJson;

    @Autowired
    private EstadioService estadioService;


    @TestConfiguration
    static class MockConfig {
        @Bean
        public EstadioService estadioService() {
            return Mockito.mock(EstadioService.class);
        }
    }


    @Test
    @DisplayName("Deve retornar status 200 quando salvar um Estádio com sucesso")
    void testSalvarEstadio() throws Exception {

        EstadioRequestDTO estadioRequestDTO = new EstadioRequestDTO();
        estadioRequestDTO.setNome("Nome do Estádio Teste");
        estadioRequestDTO.setSigla(Sigla.SP);

        Estadio estadioEntity = Conversao.dtoToEntity(estadioRequestDTO);
        estadioEntity.setId(1L);

        when(estadioService.salvarEstadio(Mockito.argThat(
                estadio -> estadio.getNome().equals(estadioRequestDTO.getNome()) &&
                        estadio.getSigla().equals(estadioRequestDTO.getSigla())
        ))).thenReturn(estadioEntity);

        EstadioResponseDTO estadioResponseDTO = Conversao.entityToDTO(estadioEntity);
        var jsonEsperado = estadioResponseDTOJson.write(estadioResponseDTO);

        mockMvc.perform(post("/estadios")
                        .contentType("application/json")
                        .content(estadioRequestDTOJson.write(estadioRequestDTO).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonEsperado.getJson()));

    }

    @Test
    @DisplayName("Deve retornar status 200 quando atualizar um Estádio com sucesso")
    void testAtualizarEstadio() throws Exception {
        Long id = 1L;
        EstadioRequestDTO estadioRequestDTO = new EstadioRequestDTO();
        estadioRequestDTO.setNome("Nome do Estádio Atualizado");
        estadioRequestDTO.setSigla(Sigla.RJ);

        Estadio estadioEditado = Conversao.dtoToEntity(estadioRequestDTO);
        estadioEditado.setId(id);

        Estadio estadioAtualizado = new Estadio();
        estadioAtualizado.setId(id);
        estadioAtualizado.setNome(estadioRequestDTO.getNome());
        estadioAtualizado.setSigla(estadioRequestDTO.getSigla());

        when(estadioService.atualizarEstadio(Mockito.argThat(
                estadio -> estadio.getId().equals(id) &&
                        estadio.getNome().equals(estadioRequestDTO.getNome()) &&
                        estadio.getSigla().equals(estadioRequestDTO.getSigla())
        ))).thenReturn(estadioAtualizado);

        EstadioResponseDTO estadioResponseDTO = Conversao.entityToDTO(estadioAtualizado);
        var jsonEsperado = estadioResponseDTOJson.write(estadioResponseDTO).getJson();

        mockMvc.perform(put("/estadios/" + id)
                        .contentType("application/json")
                        .content(estadioRequestDTOJson.write(estadioRequestDTO).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonEsperado));
    }

    @Test
    @DisplayName("Deve retornar status 200 ao buscar um Estádio por ID com sucesso")
    void testBuscarEstadioPorId() throws Exception {
        Long id = 1L;

        Estadio estadioBuscado = new Estadio();
        estadioBuscado.setId(id);
        estadioBuscado.setNome("Nome do Estádio");
        estadioBuscado.setSigla(Sigla.SP);

        when(estadioService.buscarEstadioPorId(id)).thenReturn(estadioBuscado);

        EstadioResponseDTO estadioResponseDTO = Conversao.entityToDTO(estadioBuscado);
        var jsonEsperado = estadioResponseDTOJson.write(estadioResponseDTO).getJson();

        mockMvc.perform(get("/estadios/" + id)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonEsperado));
    }

    @Test
    @DisplayName("Deve retornar status 200 ao listar estádios com sucesso")
    void testListarEstadios() throws Exception {
        Estadio estadio1 = new Estadio();
        estadio1.setId(1L);
        estadio1.setNome("Estádio 1");
        estadio1.setSigla(Sigla.SP);

        Estadio estadio2 = new Estadio();
        estadio2.setId(2L);
        estadio2.setNome("Estádio 2");
        estadio2.setSigla(Sigla.RJ);

        List<Estadio> estadios = List.of(estadio1, estadio2);
        Page<Estadio> estadioPage = new PageImpl<>(estadios);

        String nome = null;

        when(estadioService.listarEstadios(
                Mockito.eq(nome),
                Mockito.argThat(pageable ->
                        pageable.getPageSize() == 5 &&
                                pageable.getSort().getOrderFor("nome") != null
                )
        )).thenReturn(estadioPage);

        List<EstadioResponseDTO> estadioDTOs = estadios.stream()
                .map(Conversao::entityToDTO)
                .toList();


        mockMvc.perform(get("/estadios")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(estadioDTOs.size()))
                .andExpect(jsonPath("$.content[0].id").value(estadioDTOs.get(0).getId()))
                .andExpect(jsonPath("$.content[0].nome").value(estadioDTOs.get(0).getNome()))
                .andExpect(jsonPath("$.content[0].sigla").value(estadioDTOs.get(0).getSigla().toString()))
                .andExpect(jsonPath("$.content[1].id").value(estadioDTOs.get(1).getId()))
                .andExpect(jsonPath("$.content[1].nome").value(estadioDTOs.get(1).getNome()))
                .andExpect(jsonPath("$.content[1].sigla").value(estadioDTOs.get(1).getSigla().toString()));
    }
}