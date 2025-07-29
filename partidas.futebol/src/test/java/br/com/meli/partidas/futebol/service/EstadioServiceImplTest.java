package br.com.meli.partidas.futebol.service;


import br.com.meli.partidas.futebol.enums.Sigla;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.exception.IdNotFoundException;
import br.com.meli.partidas.futebol.exception.NomeExistsException;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


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

        Estadio estadioValido = estadioValido();
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

    @Test
    @DisplayName("Dado um estádio válido, deve atualizar o estádio com sucesso")
    void testAtualizarEstadio() {
        Estadio estadioSalvoNoBanco  = estadioSalvoNoBanco();

        Estadio estadioAlterado = estadioValido();
        estadioAlterado.setId(estadioSalvoNoBanco.getId());
        estadioAlterado.setNome("Nome do Estádio Alterado");
        estadioAlterado.setSigla(Sigla.RJ);

        Mockito.when(estadioRepository.existsById(estadioAlterado.getId())).thenReturn(true);
        Mockito.when(estadioRepository.existsByNome(estadioAlterado.getNome())).thenReturn(false);
        Mockito.when(estadioRepository.save(estadioAlterado)).thenReturn(estadioAlterado);

        Estadio resultado = estadioService.atualizarEstadio(estadioAlterado);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(estadioAlterado.getNome(), resultado.getNome());
        Assertions.assertEquals(estadioAlterado.getSigla(), resultado.getSigla());
    }

    @Test
    @DisplayName("Dado um id existente, deve retornar o estádio correspondente")
    void testBuscarEstadioPorId() {
        Long idExistente = 1L;

        Mockito.when(estadioRepository.existsById(idExistente)).thenReturn(true);
        Mockito.when(estadioRepository.getReferenceById(idExistente)).thenReturn(estadioSalvoNoBanco());
        Estadio resultado = estadioService.buscarEstadioPorId(idExistente);

        Assertions.assertNotNull(resultado);
        Mockito.verify(estadioRepository, Mockito.times(1)).existsById(idExistente);
        Mockito.verify(estadioRepository, Mockito.times(1)).getReferenceById(idExistente);
        Assertions.assertEquals(idExistente, resultado.getId());
        Assertions.assertEquals(estadioSalvoNoBanco().getNome(), resultado.getNome());
        Assertions.assertEquals(estadioSalvoNoBanco().getSigla(), resultado.getSigla());

    }

    @Test
    @DisplayName("Deve retornar uma lista de estádios com paginação")
    void testListarEstadios() {
        List<Estadio> estadiosSalvosNoBanco = estadiosSalvosNoBanco();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Estadio> page = new PageImpl<>(estadiosSalvosNoBanco, pageable,estadiosSalvosNoBanco.size());

        Mockito.when(estadioRepository.findAll(pageable)).thenReturn(page);
        Page<Estadio> resultado = estadioService.listarEstadios(null, pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(estadiosSalvosNoBanco.size(), resultado.getTotalElements());
        Assertions.assertEquals(2, resultado.getTotalElements());
        Assertions.assertEquals(1, resultado.getTotalPages());
        Assertions.assertEquals(resultado.getContent().contains(estadiosSalvosNoBanco.get(0)), true);
        Assertions.assertEquals(resultado.getContent().contains(estadiosSalvosNoBanco.get(1)), true);

    }

    @Test
    @DisplayName("Deve retornar todos os estádios filtrados por nome com sucesso")
    void testListarEstadiosPorNome() {
        List<Estadio> estadiosSalvos = estadiosSalvosNoBanco();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Estadio> page = new PageImpl<>(List.of(estadiosSalvos.get(0)), pageable, 1);

        Mockito.when(estadioRepository.findByNome(estadiosSalvos.get(0).getNome(), pageable)).thenReturn(page);
        Page<Estadio> resultado = estadioService.listarEstadios("Nome do Estádio", pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getTotalElements());
        Assertions.assertEquals(1, resultado.getTotalPages());
        Assertions.assertEquals(resultado.getContent().contains(estadiosSalvos.get(0)), true);
        Assertions.assertEquals(resultado.getContent().contains(estadiosSalvos.get(1)), false);
        Assertions.assertEquals("Nome do Estádio", resultado.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Dado um id inexistente, deve lançar uma exceção")
    void testIsEstadioExiste() {

        Long idInvalido = 100L;

        Mockito.when(estadioRepository.existsById(idInvalido)).thenReturn(false);
        IdNotFoundException exception = Assertions.assertThrows(
                IdNotFoundException.class, () -> estadioService.isEstadioExiste(idInvalido));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Id não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Dado que já existe um estádio com esse nome, deve lançar uma exceção")
    void testIsNomeEstadioExiste() {
        List<Estadio> estadiosSalvos = estadiosSalvosNoBanco();
        Estadio estadioAlterado = estadiosSalvos.get(0);
        estadioAlterado.setNome("Outro Estádio");
        estadioAlterado.setSigla(Sigla.RJ);

        Mockito.when(estadioRepository.existsByNome(estadioAlterado.getNome())).thenReturn(true);
        NomeExistsException exception = Assertions.assertThrows(
                NomeExistsException.class, () -> estadioService.isNomeEstadioExiste(estadioAlterado));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Já existe um estádio com esse nome", exception.getMessage());
    }

    private Estadio estadioValido() {
        Estadio estadioValido = new Estadio();
        estadioValido.setNome("Nome do Estádio");
        estadioValido.setSigla(Sigla.SP);
        return estadioValido;
    }

    private Estadio estadioSalvoNoBanco() {
        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Nome do Estádio");
        estadio.setSigla(Sigla.SP);
        return estadio;
    }


    private List<Estadio> estadiosSalvosNoBanco() {

        Estadio estadio1 = new Estadio();
        estadio1.setId(1L);
        estadio1.setNome("Nome do Estádio");
        estadio1.setSigla(Sigla.SP);

        Estadio estadio2 = new Estadio();
        estadio2.setId(2L);
        estadio2.setNome("Outro Estádio");
        estadio2.setSigla(Sigla.RJ);

        return List.of(estadio1, estadio2);
    }
}