package br.com.meli.partidas.futebol.utils;

import br.com.meli.partidas.futebol.dto.Sigla;
import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ClubeResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;


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
    void testDtoToEntity() {
    }

    @Test
    void testEntityToDTO() {
    }

    @Test
    void testDtoToEntity1() {
    }

    @Test
    void testEntityToDTO1() {
    }
}