package br.com.meli.partidas.futebol.utils;

import br.com.meli.partidas.futebol.dto.response.RetrospectoDoClubeResponseDTO;
import br.com.meli.partidas.futebol.enums.Sigla;
import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.request.EstadioRequestDTO;
import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ClubeResponseDTO;
import br.com.meli.partidas.futebol.dto.response.EstadioResponseDTO;
import br.com.meli.partidas.futebol.dto.response.PartidaResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


class ConversaoTest {

    @Test
    @DisplayName("Dado um ClubeRequestDTO válido, deve retornar um Clube sem id e sem partidas")
    void testDtoToEntityClube() {

        ClubeRequestDTO clubeRequestDTO = new ClubeRequestDTO();
        clubeRequestDTO.setNome("Nome do Clube");
        clubeRequestDTO.setSigla(Sigla.SP);
        clubeRequestDTO.setDataCriacao(LocalDate.now());
        clubeRequestDTO.setAtivo(true);

        Clube resultado = Conversao.dtoToEntity(clubeRequestDTO);

        Assertions.assertNotNull(resultado);
        Assertions.assertNull(resultado.getId());
        Assertions.assertNull(resultado.getPartidasMandante());
        Assertions.assertNull(resultado.getPartidasVisitante());
        Assertions.assertEquals(clubeRequestDTO.getNome(), resultado.getNome());
        Assertions.assertEquals(clubeRequestDTO.getSigla(), resultado.getSigla());
        Assertions.assertEquals(clubeRequestDTO.getDataCriacao(), resultado.getDataCriacao());
        Assertions.assertEquals(clubeRequestDTO.getAtivo(), resultado.getAtivo());
    }

    @Test
    @DisplayName("Dado um Clube válido, deve retornar um ClubeResponseDTO")
    void testEntityToDTOClube() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setNome("Nome do Clube");
        clube.setSigla(Sigla.SP);
        clube.setDataCriacao(LocalDate.now());
        clube.setAtivo(true);
        clube.setPartidasMandante(null);
        clube.setPartidasVisitante(null);

        ClubeResponseDTO resultado = Conversao.entityToDTO(clube);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(clube.getId(), resultado.getId());
        Assertions.assertEquals(clube.getNome(), resultado.getNome());
        Assertions.assertEquals(clube.getSigla(), resultado.getSigla());
        Assertions.assertEquals(clube.getDataCriacao(), resultado.getDataCriacao());
        Assertions.assertEquals(clube.getAtivo(), resultado.getAtivo());

    }

    @Test
    @DisplayName("Dado um EstadioRequestDTO válido, deve retornar um Estadio sem id")
    void testDtoToEntityEstadio() {
        EstadioRequestDTO estadioRequestDTO = new EstadioRequestDTO();
        estadioRequestDTO.setNome("Nome do Estádio");
        estadioRequestDTO.setSigla(Sigla.SP);

        Estadio resultado = Conversao.dtoToEntity(estadioRequestDTO);

        Assertions.assertNotNull(resultado);
        Assertions.assertNull(resultado.getId());
        Assertions.assertEquals(estadioRequestDTO.getNome(), resultado.getNome());
        Assertions.assertEquals(estadioRequestDTO.getSigla(), resultado.getSigla());

    }

    @Test
    @DisplayName("Dado um Estadio válido, deve retornar um EstadioResponseDTO")
    void testEntityToDTO() {
        Estadio estadio = estadio();

        EstadioResponseDTO resultado = Conversao.entityToDTO(estadio);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(estadio.getId(), resultado.getId());
        Assertions.assertEquals(estadio.getNome(), resultado.getNome());
        Assertions.assertEquals(estadio.getSigla(), resultado.getSigla());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO, Clube Mandante, Clube Visitante e Estádio válidos, deve retornar uma Partida sem id")
    void testDtoToEntityPartida() {
        PartidaRequestDTO partidaRequestDTO = new PartidaRequestDTO();
        partidaRequestDTO.setIdClubeMandante(1L);
        partidaRequestDTO.setIdClubeVisitante(2L);
        partidaRequestDTO.setGolsMandante(1);
        partidaRequestDTO.setGolsVisitante(0);
        partidaRequestDTO.setIdEstadio(1L);
        partidaRequestDTO.setDataHoraPartida(LocalDateTime.now());

        Clube clubeMandante = clubeMandante();
        Clube clubeVisitante = clubeVisitante();
        Estadio estadio = estadio();

        Partida resultado = Conversao.dtoToEntity(partidaRequestDTO, clubeMandante, clubeVisitante, estadio);

        Assertions.assertNotNull(resultado);
        Assertions.assertNull(resultado.getId());
        Assertions.assertEquals(partidaRequestDTO.getIdClubeMandante(), resultado.getIdClubeMandante().getId());
        Assertions.assertEquals(partidaRequestDTO.getIdClubeVisitante(), resultado.getIdClubeVisitante().getId());
        Assertions.assertEquals(partidaRequestDTO.getGolsMandante(), resultado.getGolsMandante());
        Assertions.assertEquals(partidaRequestDTO.getGolsVisitante(), resultado.getGolsVisitante());
        Assertions.assertEquals("Clube Mandante 1 x 0 Clube Visitante", resultado.getResultado());
        Assertions.assertEquals(partidaRequestDTO.getIdEstadio(), resultado.getIdEstadio().getId());
        Assertions.assertEquals(partidaRequestDTO.getDataHoraPartida(), resultado.getDataHoraPartida());

    }

    @Test
    @DisplayName("Dado uma Partida válida, deve retornar uma PartidaResponseDTO")
    void testEntityToDTO_Partida() {

        Partida partida = new Partida();
        partida.setId(1L);
        partida.setIdClubeMandante(clubeMandante());
        partida.setIdClubeVisitante(clubeVisitante());
        partida.setGolsMandante(1);
        partida.setGolsVisitante(0);
        partida.setResultado("Clube Mandante 1 x 0 Clube Visitante");
        partida.setIdEstadio(estadio());

        PartidaResponseDTO resultado = Conversao.entityToDTO(partida);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partida.getId(), resultado.getId());
        Assertions.assertEquals(partida.getIdClubeMandante().getId(), resultado.getIdClubeMandante());
        Assertions.assertEquals(partida.getIdClubeVisitante().getId(), resultado.getIdClubeVisitante());
        Assertions.assertEquals(partida.getResultado(), resultado.getResultado());
        Assertions.assertEquals(partida.getIdEstadio().getId(), resultado.getIdEstadio());
        Assertions.assertEquals(partida.getDataHoraPartida(), resultado.getDataHoraPartida());
    }

    @Test
    @DisplayName("Dado um Clube válido, deve retornar um RetrospectoDoClubeResponseDTO")
    void testEntityToRetrospectoDTO() {
        Clube clube = clubeMandante();

        RetrospectoDoClubeResponseDTO resultado = Conversao.entityToRetrospectoDTO(clube);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(clube.getNome(), resultado.getNomeClube());
        Assertions.assertEquals(clube.getVitorias(), resultado.getVitorias());
        Assertions.assertEquals(clube.getEmpates(), resultado.getEmpates());
        Assertions.assertEquals(clube.getDerrotas(), resultado.getDerrotas());
        Assertions.assertEquals(clube.getGolsMarcados(), resultado.getGolsMarcados());
        Assertions.assertEquals(clube.getGolsSofridos(), resultado.getGolsSofridos());
    }

    private Clube clubeMandante() {
        Clube clubeMandante = new Clube();
        clubeMandante.setId(1L);
        clubeMandante.setNome("Clube Mandante");
        clubeMandante.setSigla(Sigla.SP);
        clubeMandante.setDataCriacao(LocalDate.now());
        clubeMandante.setAtivo(true);
        clubeMandante.setPartidasMandante(List.of());
        clubeMandante.setPartidasVisitante(List.of());
        return clubeMandante;
    }

    private Clube clubeVisitante() {
        Clube clubeVisitante = new Clube();
        clubeVisitante.setId(2L);
        clubeVisitante.setNome("Clube Visitante");
        clubeVisitante.setSigla(Sigla.RJ);
        clubeVisitante.setDataCriacao(LocalDate.now());
        clubeVisitante.setAtivo(true);
        clubeVisitante.setPartidasMandante(List.of());
        clubeVisitante.setPartidasVisitante(List.of());
        return clubeVisitante;
    }

    private Estadio estadio() {
        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Nome do Estádio");
        estadio.setSigla(Sigla.SP);
        return estadio;
    }
}