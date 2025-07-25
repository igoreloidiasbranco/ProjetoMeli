package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.Sigla;
import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import br.com.meli.partidas.futebol.repository.PartidaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


class PartidaServiceImplTest {

    private PartidaService partidaService;
    private ClubeService clubeService;
    private PartidaRepository partidaRepository;
    private ClubeRepository clubeRepository;
    private EstadioRepository estadioRepository;


    @BeforeEach
    void setUp() {
        clubeService = Mockito.mock(ClubeService.class);
        clubeRepository = Mockito.mock(ClubeRepository.class);
        estadioRepository = Mockito.mock(EstadioRepository.class);
        partidaRepository = Mockito.mock(PartidaRepository.class);
        partidaService = new PartidaServiceImpl(partidaRepository, clubeRepository, estadioRepository, clubeService);
    }


    @Test
    @DisplayName("Dado um PartidaRequestDTO válido, deve retornar uma Partida validada")
    void testValidarPartidaRequestDTO() {
        PartidaRequestDTO partidaRequestDTO = partidaRequestDTO();
        Long partidaAtual = null;

        Mockito.when(clubeRepository.existsById(partidaRequestDTO.getIdClubeMandante())).thenReturn(true);
        Mockito.when(clubeRepository.existsById(partidaRequestDTO.getIdClubeVisitante())).thenReturn(true);
        Mockito.when(clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeMandante())).thenReturn(clubeUm());
        Mockito.when(clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeVisitante())).thenReturn(clubeDois());
        Mockito.when(estadioRepository.findById(partidaRequestDTO.getIdEstadio())).thenReturn(Optional.of(estadio()));

        Partida resultado = partidaService.validarPartida(partidaRequestDTO, partidaAtual);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidaRequestDTO.getIdClubeMandante(), resultado.getIdClubeMandante().getId());
        Assertions.assertEquals(partidaRequestDTO.getIdClubeVisitante(), resultado.getIdClubeVisitante().getId());
        Assertions.assertEquals(partidaRequestDTO.getGolsMandante(), resultado.getGolsMandante());
        Assertions.assertEquals(partidaRequestDTO.getGolsVisitante(), resultado.getGolsVisitante());
        Assertions.assertEquals(partidaRequestDTO.getIdEstadio(), resultado.getIdEstadio().getId());
        Assertions.assertEquals(partidaRequestDTO.getDataHoraPartida(), resultado.getDataHoraPartida());
    }


    @Test
    @DisplayName("Dado uma Partida validada, deve retornar uma Partida salva com sucesso")
    void testSalvarPartida() {
        Partida partidaValidada = partidaSalvaNoBanco();
        partidaValidada.setId(null);

        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partidaSalvaNoBanco());
        Mockito.when(clubeRepository.findById(1L)).thenReturn(Optional.of(clubeUm()));
        Mockito.when(clubeRepository.findById(2L)).thenReturn(Optional.of(clubeDois()));
        Partida resultado = partidaService.salvarPartida(partidaValidada);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidaValidada.getIdClubeMandante().getId(), resultado.getIdClubeMandante().getId());
        Assertions.assertEquals(partidaValidada.getIdClubeVisitante().getId(), resultado.getIdClubeVisitante().getId());
        Assertions.assertEquals(partidaValidada.getGolsMandante(), resultado.getGolsMandante());
        Assertions.assertEquals(partidaValidada.getGolsVisitante(), resultado.getGolsVisitante());
        Assertions.assertEquals(partidaValidada.getIdEstadio().getId(), resultado.getIdEstadio().getId());
        Assertions.assertEquals(partidaValidada.getDataHoraPartida().withNano(0), resultado.getDataHoraPartida().withNano(0));
        Assertions.assertEquals(partidaValidada.getResultado(), resultado.getResultado());
    }

    @Test
    @DisplayName("Dado uma Partida editada validada, deve retornar uma Partida atualizada com sucesso")
    void testAtualizarPartida() {
        Partida partidaEditada = partidaSalvaNoBanco();
        partidaEditada.setIdClubeMandante(clubeDois());
        partidaEditada.setIdClubeVisitante(clubeUm());
        partidaEditada.setGolsMandante(0);
        partidaEditada.setGolsVisitante(1);
        partidaEditada.setResultado("0x1");


        Mockito.when(partidaRepository.findById(partidaEditada.getId())).thenReturn(Optional.of(partidaSalvaNoBanco()));
        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partidaEditada);
        Mockito.when(clubeRepository.findById(1L)).thenReturn(Optional.of(clubeUm()));
        Mockito.when(clubeRepository.findById(2L)).thenReturn(Optional.of(clubeDois()));
        Partida resultado = partidaService.atualizarPartida(partidaEditada);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidaEditada.getIdClubeMandante().getId(), resultado.getIdClubeMandante().getId());
        Assertions.assertEquals(partidaEditada.getIdClubeVisitante().getId(), resultado.getIdClubeVisitante().getId());
        Assertions.assertEquals(partidaEditada.getGolsMandante(), resultado.getGolsMandante());
        Assertions.assertEquals(partidaEditada.getGolsVisitante(), resultado.getGolsVisitante());
        Assertions.assertEquals(partidaEditada.getIdEstadio().getId(), resultado.getIdEstadio().getId());
        Assertions.assertEquals(partidaEditada.getDataHoraPartida().withNano(0), resultado.getDataHoraPartida().withNano(0));
        Assertions.assertEquals(partidaEditada.getResultado(), resultado.getResultado());
    }

    @Test
    @DisplayName("Dado um id de partida, deve deletar a partida com sucesso")
    void testDeletarPartida() {
        Long idPartida = 1L;
        Partida partida = partidaSalvaNoBanco();

        Mockito.when(partidaRepository.findById(idPartida)).thenReturn(Optional.of(partida));
        Mockito.when(clubeRepository.findById(partida.getIdClubeMandante().getId())).thenReturn(Optional.of(clubeUm()));
        Mockito.when(clubeRepository.findById(partida.getIdClubeVisitante().getId())).thenReturn(Optional.of(clubeDois()));
        Mockito.doNothing().when(partidaRepository).delete(partida);

        Assertions.assertDoesNotThrow(() -> {
            partidaService.deletarPartida(partida.getId());
        });

        Mockito.verify(clubeRepository, Mockito.times(1)).findById(partida.getIdClubeMandante().getId());
        Mockito.verify(clubeRepository, Mockito.times(1)).findById(partida.getIdClubeVisitante().getId());
        Mockito.verify(partidaRepository, Mockito.times(1)).delete(partida);
    }

    @Test
    @DisplayName("Dado um id de partida, deve buscar a partida com sucesso")
    void testBuscarPartidaPorId() {
        Long idPartida = 1L;
        Partida partidaSalvaNoBanco = partidaSalvaNoBanco();

        Mockito.when(partidaRepository.findById(idPartida)).thenReturn(Optional.of(partidaSalvaNoBanco));

        Partida resultado = partidaService.buscarPartidaPorId(idPartida);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidaSalvaNoBanco.getId(), resultado.getId());
        Assertions.assertEquals(partidaSalvaNoBanco.getIdClubeMandante().getId(), resultado.getIdClubeMandante().getId());
        Assertions.assertEquals(partidaSalvaNoBanco.getIdClubeVisitante().getId(), resultado.getIdClubeVisitante().getId());
        Assertions.assertEquals(partidaSalvaNoBanco.getGolsMandante(), resultado.getGolsMandante());
        Assertions.assertEquals(partidaSalvaNoBanco.getGolsVisitante(), resultado.getGolsVisitante());
        Assertions.assertEquals(partidaSalvaNoBanco.getIdEstadio().getId(), resultado.getIdEstadio().getId());
        Assertions.assertEquals(partidaSalvaNoBanco.getDataHoraPartida().withNano(0), resultado.getDataHoraPartida().withNano(0));
        Assertions.assertEquals(partidaSalvaNoBanco.getResultado(), resultado.getResultado());
    }

    @Test
    @DisplayName("Deve retornar uma lista de partidas")
    void testListarPartida() {
        Pageable paginacao = PageRequest.of(0, 10);
        Page<Partida> partidas = new PageImpl<>(List.of(partidaSalvaNoBanco()));

        Mockito.when(partidaRepository.findAll(paginacao)).thenReturn(partidas);
        Page<Partida> resultado = partidaService.listarPartidas(null, null, paginacao);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidas, resultado);
    }

    @Test
    @DisplayName("Deve retornar uma lista de partidas filtrada por nome de clube")
    void testListarPartidaPorNomeClube() {
        Pageable paginacao = PageRequest.of(0, 10);
        Page<Partida> partidas = new PageImpl<>(List.of(partidaSalvaNoBanco()));

        Mockito.when(partidaRepository.listarPartidasPorClube("Clube Um", paginacao)).thenReturn(partidas);
        Page<Partida> resultado = partidaService.listarPartidas("Clube Um", null, paginacao);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidas.getTotalPages(), resultado.getTotalPages());
        Assertions.assertEquals(partidas.getTotalElements(), resultado.getTotalElements());
        Assertions.assertEquals(partidas, resultado);

    }

    @Test
    @DisplayName("Deve retornar uma lista de partidas filtrada por nome de estádio")
    void testListarPartidaPorNomeEstadio() {
        Pageable paginacao = PageRequest.of(0, 10);
        Page<Partida> partidas = new PageImpl<>(List.of(partidaSalvaNoBanco()));

        Mockito.when(partidaRepository.listarPartidasPorEstadio("Nome do Estádio", paginacao)).thenReturn(partidas);
        Page<Partida> resultado = partidaService.listarPartidas(null, "Nome do Estádio", paginacao);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(partidas.getTotalPages(), resultado.getTotalPages());
        Assertions.assertEquals(partidas.getTotalElements(), resultado.getTotalElements());
        Assertions.assertEquals(partidas, resultado);
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com clube mandante que não existe, deve lançar uma exceção")
    void testIsClubesExistemComClubeMandanteInexistente() {
        PartidaRequestDTO partidaRequestDTO = partidaRequestDTO();
        partidaRequestDTO.setIdClubeMandante(3L);

        Mockito.when(clubeRepository.existsById(partidaRequestDTO.getIdClubeMandante())).thenReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isClubesExistem(partidaRequestDTO);
        });

        Assertions.assertNotNull(exception);
        Mockito.verify(clubeRepository, Mockito.times(1)).existsById(partidaRequestDTO.getIdClubeMandante());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Clube mandante não encontrado", exception.getReason());
    }


    @Test
    @DisplayName("Dado um PartidaRequestDTO com clube visitante que não existe, deve lançar uma exceção")
    void testIsClubesExistemComClubeVisitanteInexistente() {
        PartidaRequestDTO partidaRequestDTO = partidaRequestDTO();
        partidaRequestDTO.setIdClubeVisitante(3L);

        Mockito.when(clubeRepository.existsById(partidaRequestDTO.getIdClubeMandante())).thenReturn(true);
        Mockito.when(clubeRepository.existsById(partidaRequestDTO.getIdClubeVisitante())).thenReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isClubesExistem(partidaRequestDTO);
        });

        Assertions.assertNotNull(exception);
        Mockito.verify(clubeRepository, Mockito.times(1)).existsById(partidaRequestDTO.getIdClubeMandante());
        Mockito.verify(clubeRepository, Mockito.times(1)).existsById(partidaRequestDTO.getIdClubeVisitante());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Clube visitante não encontrado", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com Ids de clube mandante e visitante iguais, deve lançar uma exceção")
    void testIsClubesIguais() {

        PartidaRequestDTO partidaComTimesIguais = partidaRequestDTO();
        partidaComTimesIguais.setIdClubeMandante(1L);
        partidaComTimesIguais.setIdClubeVisitante(1L);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isClubesDiferentes(partidaComTimesIguais);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Clube mandante e visitante não podem ser iguais", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com Ids de clube mandante e visitante diferentes, não deve lançar uma exceção")
    void testIsClubesDiferentes() {

        PartidaRequestDTO partidaComTimesDiferentes = partidaRequestDTO();

        Assertions.assertDoesNotThrow(() -> {
            partidaService.isClubesDiferentes(partidaComTimesDiferentes);
        });
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com gols negativo do clube mandante, deve lançar uma exceção")
    void testIsGolsNegativosClubeMandante() {
        PartidaRequestDTO partidaComGolsNegativosMandante = partidaRequestDTO();
        partidaComGolsNegativosMandante.setGolsMandante(-1);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isGolsPositivos(partidaComGolsNegativosMandante.getGolsMandante(), partidaComGolsNegativosMandante.getGolsVisitante());
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Gols não podem ser negativos", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com gols negativo do clube visitante, deve lançar uma exceção")
    void testIsGolsNegativosClubeVisitante() {
        PartidaRequestDTO partidaComGolsNegativosVisitante = partidaRequestDTO();
        partidaComGolsNegativosVisitante.setGolsVisitante(-1);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isGolsPositivos(partidaComGolsNegativosVisitante.getGolsMandante(), partidaComGolsNegativosVisitante.getGolsVisitante());
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Gols não podem ser negativos", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com gols positivos, não deve lançar uma exceção")
    void testIsGolsPositivos() {
        PartidaRequestDTO partidaComGolsPositivos = partidaRequestDTO();

        Assertions.assertDoesNotThrow(() -> {
            partidaService.isGolsPositivos(partidaComGolsPositivos.getGolsMandante(), partidaComGolsPositivos.getGolsVisitante());
        });
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com data de partida antes da criação do clube mandante, deve lançar uma exceção")
    void testIsDataHoraPartidaAntesCriacaoClubeMandante() {
        Clube clubeMandante = clubeUm();
        clubeMandante.setDataCriacao(LocalDate.now().plusDays(1));
        LocalDate dataPartida = LocalDate.now();

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isDataHoraAntesCriacaoClube(clubeMandante, clubeDois(), dataPartida);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Data da partida não pode ser anterior à data de criação do clube mandante", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com data de partida antes da criação do clube visitante, deve lançar uma exceção")
    void testIsDataHoraPartidaAntesCriacaoClubeVisitante() {
        Clube clubeVisitante = clubeDois();
        clubeVisitante.setDataCriacao(LocalDate.now().plusDays(1));
        LocalDate dataPartida = LocalDate.now();

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isDataHoraAntesCriacaoClube(clubeUm(), clubeVisitante, dataPartida);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Data da partida não pode ser anterior à data de criação do clube visitante", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com data de partida válida, não deve lançar uma exceção")
    void testIsDataHoraPartidaValida() {
        Clube clubeMandante = clubeUm();
        Clube clubeVisitante = clubeDois();
        LocalDate dataPartida = LocalDate.now().plusDays(1);

        Assertions.assertDoesNotThrow(() -> {
            partidaService.isDataHoraAntesCriacaoClube(clubeMandante, clubeVisitante, dataPartida);
        });
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com clube mandante inativo, deve lançar uma exceção")
    void testIsClubesAtivosComClubeMandanteInativo() {
        Clube clubeMandante = clubeUm();
        clubeMandante.setAtivo(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isClubesAtivos(clubeMandante, clubeDois());
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube mandante não está ativo", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com clube visitante inativo, deve lançar uma exceção")
    void testIsClubesAtivosComClubeVisitanteInativo() {
        Clube clubeVisitante = clubeUm();
        clubeVisitante.setAtivo(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isClubesAtivos(clubeUm(), clubeVisitante);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube visitante não está ativo", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com clubes ativos, não deve lançar uma exceção")
    void testIsClubesAtivos() {
        Clube clubeMandante = clubeUm();
        Clube clubeVisitante = clubeDois();

        Assertions.assertDoesNotThrow(() -> {
            partidaService.isClubesAtivos(clubeMandante, clubeVisitante);
        });
    }

    @Test
    @DisplayName("Dado um clube mandante com partida como mandante marcada menor que 48 horas de diferença, deve lançar uma exceção")
    void testIsClubeMandanteComPartidaComoMandanteMenorQue48HorasDiferenca() {
        Clube clubeMandante = clubeUm();
        clubeMandante.setPartidasMandante(List.of(new Partida(1L, clubeMandante, clubeDois(), 1, 0, "1x0", estadio(), LocalDateTime.now())));
        Clube clubeVisitante = clubeDois();
        LocalDateTime dataHoraPartida = LocalDateTime.now().plusHours(24);
        Long idPartidaAtual = null; // Simulando que é uma nova partida

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isPartidaAposIntervalo(clubeMandante, clubeVisitante, dataHoraPartida, idPartidaAtual);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube mandante já possue outra partida cadastrada com diferença menor do que 48 horas em relação a esta", exception.getReason());
    }

    @Test
    @DisplayName("Dado um clube mandante com partida como visitante marcada menor que 48 horas de diferença, deve lançar uma exceção")
    void testIsClubeMandanteComPartidaComoVisitanteMenorQue48HorasDiferenca() {
        Clube clubeMandante = clubeUm();
        clubeMandante.setPartidasVisitante(List.of(new Partida(1L, clubeMandante, clubeDois(), 1, 0, "1x0", estadio(), LocalDateTime.now())));
        Clube clubeVisitante = clubeDois();
        LocalDateTime dataHoraPartida = LocalDateTime.now().plusHours(24);
        Long idPartidaAtual = null; // Simulando que é uma nova partida

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isPartidaAposIntervalo(clubeMandante, clubeVisitante, dataHoraPartida, idPartidaAtual);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube mandante já possue outra partida cadastrada com diferença menor do que 48 horas em relação a esta", exception.getReason());
    }

    @Test
    @DisplayName("Dado um clube visitante com partida marcada menor que 48 horas de diferença, deve lançar uma exceção")
    void testIsClubeVisitanteComPartidaComoMandanteMenorQue48HorasDiferenca() {
        Clube clubeMandante = clubeUm();
        Clube clubeVisitante = clubeDois();
        clubeVisitante.setPartidasMandante(List.of(new Partida(1L, clubeMandante, clubeVisitante, 1, 0, "1x0", estadio(), LocalDateTime.now())));
        LocalDateTime dataHoraPartida = LocalDateTime.now().plusHours(24);
        Long idPartidaAtual = null; // Simulando que é uma nova partida

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isPartidaAposIntervalo(clubeMandante, clubeVisitante, dataHoraPartida, idPartidaAtual);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube visitante já possue outra partida cadastrada com diferença menor do que 48 horas em relação a esta", exception.getReason());
    }

    @Test
    @DisplayName("Dado um clube visitante com partida marcada menor que 48 horas de diferença, deve lançar uma exceção")
    void testIsClubeVisitanteComPartidaComoVisitanteMenorQue48HorasDiferenca() {
        Clube clubeMandante = clubeUm();
        Clube clubeVisitante = clubeDois();
        clubeVisitante.setPartidasVisitante(List.of(new Partida(1L, clubeMandante, clubeVisitante, 1, 0, "1x0", estadio(), LocalDateTime.now())));
        LocalDateTime dataHoraPartida = LocalDateTime.now().plusHours(24);
        Long idPartidaAtual = null; // Simulando que é uma nova partida

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isPartidaAposIntervalo(clubeMandante, clubeVisitante, dataHoraPartida, idPartidaAtual);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube visitante já possue outra partida cadastrada com diferença menor do que 48 horas em relação a esta", exception.getReason());
    }

    @Test
    @DisplayName("Deve lançar uma exceção se o estádio já tiver uma partida marcada na data")
    void testIsEstadioComPartidaNaData() {
        Estadio estadio = estadio();
        estadio.setPartidas(List.of(new Partida(1L, clubeUm(), clubeDois(), 1, 0, "1x0", estadio, LocalDateTime.now())));
        Long idPartidaAtual = null;


        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isEstadioSemPartida(estadio, LocalDate.now(), idPartidaAtual);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Estádio já possui partida marcada para esta data", exception.getReason());
    }

    @Test
    @DisplayName("Deve lançar exceção se o estádio não existir")
    void testIsEstadioNaoExistente() {
        Estadio estadio = estadio();
        estadio.setId(999L);

        Mockito.when(estadioRepository.findById(estadio.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.buscarEstadio(estadio.getId());
        });

        Assertions.assertNotNull(exception);
        Mockito.verify(estadioRepository, Mockito.times(1)).findById(estadio.getId());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        Assertions.assertEquals("Estádio não encontrado", exception.getReason());
    }

    @Test
    @DisplayName("Deve lançar exceção se a partida não existir")
    void testIsPartidaNaoExistente() {
        Long idPartidaInexistente = 999L;

        Mockito.when(partidaRepository.findById(idPartidaInexistente)).thenReturn(Optional.empty());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.buscarPartidaPorId(idPartidaInexistente);
        });

        Assertions.assertNotNull(exception);
        Mockito.verify(partidaRepository, Mockito.times(1)).findById(idPartidaInexistente);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals("Partida não encontrada", exception.getReason());
    }

    private PartidaRequestDTO partidaRequestDTO() {
        PartidaRequestDTO partidaRequestDTO = new PartidaRequestDTO();
        partidaRequestDTO.setIdClubeMandante(1L);
        partidaRequestDTO.setIdClubeVisitante(2L);
        partidaRequestDTO.setGolsMandante(1);
        partidaRequestDTO.setGolsVisitante(0);
        partidaRequestDTO.setIdEstadio(1L);
        partidaRequestDTO.setDataHoraPartida(LocalDateTime.now());
        return partidaRequestDTO;
    }

    private Clube clubeUm() {
        Clube clubeMandante = new Clube();
        clubeMandante.setId(1L);
        clubeMandante.setNome("Clube Um");
        clubeMandante.setSigla(Sigla.SP);
        clubeMandante.setDataCriacao(LocalDate.now());
        clubeMandante.setAtivo(true);
        clubeMandante.setPartidasMandante(new ArrayList<>());
        clubeMandante.setPartidasVisitante(new ArrayList<>());
        return clubeMandante;
    }

    private Clube clubeDois() {
        Clube clubeVisitante = new Clube();
        clubeVisitante.setId(2L);
        clubeVisitante.setNome("Clube Dois");
        clubeVisitante.setSigla(Sigla.RJ);
        clubeVisitante.setDataCriacao(LocalDate.now());
        clubeVisitante.setAtivo(true);
        clubeVisitante.setPartidasMandante(new ArrayList<>());
        clubeVisitante.setPartidasVisitante(new ArrayList<>());
        return clubeVisitante;
    }

    private Estadio estadio() {
        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Nome do Estádio");
        estadio.setSigla(Sigla.SP);
        estadio.setPartidas(new ArrayList<>());
        return estadio;
    }

    private Partida partidaSalvaNoBanco() {
        Partida partida = new Partida();
        partida.setId(1L);
        partida.setIdClubeMandante(clubeUm());
        partida.setIdClubeVisitante(clubeDois());
        partida.setGolsMandante(2);
        partida.setGolsVisitante(1);
        partida.setResultado("2x1");
        partida.setIdEstadio(estadio());
        partida.setDataHoraPartida(LocalDateTime.now());
        return partida;
    }
}