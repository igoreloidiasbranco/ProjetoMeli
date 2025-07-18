package br.com.meli.partidas.futebol.service;


import br.com.meli.partidas.futebol.dto.Sigla;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class EstadioServiceImplTest {


    EstadioService estadioService;
    EstadioRepository estadioRepository;

    @BeforeEach
    void setUp() {
        estadioRepository = Mockito.mock(EstadioRepository.class);
        estadioService = new EstadioServiceImpl(estadioRepository);
    }

    @Test
    @DisplayName("Dado um estádio válido, deve salvar o estádio com sucesso")
    void testSalvarEstadio() {
        Estadio estadioValido = new Estadio();
        estadioValido.setNome("Nome do Estádio");
        estadioValido.setSigla(Sigla.SP);

        Estadio estadioSalvoNoBanco = estadioSalvoNoBanco();

        Mockito.when(estadioRepository.existsByNome(estadioValido.getNome())).thenReturn(false);
        Mockito.when(estadioRepository.save(estadioValido)).thenReturn(estadioSalvoNoBanco);
        Estadio resultado = estadioService.salvarEstadio(estadioValido);


        Mockito.verify(estadioRepository, Mockito.times(1)).save(estadioValido);
        Assertions.assertNotNull(resultado);
        Assertions.assertNotNull(resultado.getId());
        Assertions.assertEquals(estadioSalvoNoBanco.getNome(), resultado.getNome());
        Assertions.assertEquals(estadioSalvoNoBanco.getSigla(), resultado.getSigla());

    }

    private Estadio estadioSalvoNoBanco() {
        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Nome do Estádio");
        estadio.setSigla(Sigla.SP);
        return estadio;
    }

}