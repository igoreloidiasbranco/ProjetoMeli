package br.com.meli.partidas.futebol.utils;

import br.com.meli.partidas.futebol.dto.response.RetrospectoDoClubeContraOutroResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static br.com.meli.partidas.futebol.enums.Sigla.RJ;
import static br.com.meli.partidas.futebol.enums.Sigla.SP;

class BuilderTest {

    @Test
    @DisplayName("Deve criar um retrospectoDoClubeContraOutroResponseDTO vazio")
    void testRetrospectoDoClubeContraOutroResponseDTOVazio() {
        Clube clube1 = new Clube();
        clube1.setId(1L);
        clube1.setNome("Nome do clube1");
        clube1.setSigla(SP);
        clube1.setDataCriacao(LocalDate.of(1930, 1, 25));
        clube1.setAtivo(true);

        Clube clube2 = new Clube();
        clube2.setId(2L);
        clube2.setNome("Nome do clube2");
        clube2.setSigla(RJ);
        clube2.setDataCriacao(LocalDate.of(1935, 1, 25));
        clube2.setAtivo(true);

        RetrospectoDoClubeContraOutroResponseDTO resultado = Builder.retrospectoDoClubeContraOutroResponseDTOVazio(clube1, clube2);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Nome do clube1", resultado.getNomeClube());
        Assertions.assertEquals("Nome do clube2", resultado.getNomeAdversario());
        Assertions.assertEquals(0, resultado.getVitorias());
        Assertions.assertEquals(0, resultado.getEmpates());
        Assertions.assertEquals(0, resultado.getDerrotas());
        Assertions.assertEquals(0, resultado.getGolsMarcados());
        Assertions.assertEquals(0, resultado.getGolsSofridos());
    }
}