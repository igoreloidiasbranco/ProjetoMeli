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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


class PartidaServiceImplTest {

    private PartidaService partidaService;
    private PartidaRepository partidaRepository;
    private ClubeRepository clubeRepository;
    private EstadioRepository estadioRepository;


    @BeforeEach
    void setUp() {
        clubeRepository = Mockito.mock(ClubeRepository.class);
        estadioRepository = Mockito.mock(EstadioRepository.class);
        partidaRepository = Mockito.mock(PartidaRepository.class);
        partidaService = new PartidaServiceImpl(partidaRepository, clubeRepository, estadioRepository);
    }


    @Test
    @DisplayName("Dado um PartidaRequestDTO válido, deve retornar uma Partida validada")
    void testValidarPartidaRequestDTO() {
        PartidaRequestDTO partidaRequestDTO = partidaRequestDTO();
        Long partidaAtual = null;

        Mockito.when(clubeRepository.existsById(partidaRequestDTO.getIdClubeMandante())).thenReturn(true);
        Mockito.when(clubeRepository.existsById(partidaRequestDTO.getIdClubeVisitante())).thenReturn(true);
        Mockito.when(clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeMandante())).thenReturn(clubeMandante());
        Mockito.when(clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeVisitante())).thenReturn(clubeVisitante());
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
        Clube clubeMandante = clubeMandante();
        clubeMandante.setDataCriacao(LocalDate.now().plusDays(1));
        LocalDate dataPartida = LocalDate.now();

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isDataHoraAntesCriacaoClube(clubeMandante, clubeVisitante(), dataPartida);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Data da partida não pode ser anterior à data de criação do clube mandante", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com data de partida antes da criação do clube visitante, deve lançar uma exceção")
    void testIsDataHoraPartidaAntesCriacaoClubeVisitante() {
        Clube clubeVisitante = clubeVisitante();
        clubeVisitante.setDataCriacao(LocalDate.now().plusDays(1));
        LocalDate dataPartida = LocalDate.now();

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isDataHoraAntesCriacaoClube(clubeMandante(), clubeVisitante, dataPartida);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Data da partida não pode ser anterior à data de criação do clube visitante", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com data de partida válida, não deve lançar uma exceção")
    void testIsDataHoraPartidaValida() {
        Clube clubeMandante = clubeMandante();
        Clube clubeVisitante = clubeVisitante();
        LocalDate dataPartida = LocalDate.now().plusDays(1);

        Assertions.assertDoesNotThrow(() -> {
            partidaService.isDataHoraAntesCriacaoClube(clubeMandante, clubeVisitante, dataPartida);
        });
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com clube mandante inativo, deve lançar uma exceção")
    void testIsClubesAtivosComClubeMandanteInativo() {
        Clube clubeMandante = clubeMandante();
        clubeMandante.setAtivo(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isClubesAtivos(clubeMandante, clubeVisitante());
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube mandante não está ativo", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com clube visitante inativo, deve lançar uma exceção")
    void testIsClubesAtivosComClubeVisitanteInativo() {
        Clube clubeVisitante = clubeMandante();
        clubeVisitante.setAtivo(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isClubesAtivos(clubeMandante(), clubeVisitante);
        });

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        Assertions.assertEquals("Clube visitante não está ativo", exception.getReason());
    }

    @Test
    @DisplayName("Dado um PartidaRequestDTO com clubes ativos, não deve lançar uma exceção")
    void testIsClubesAtivos() {
        Clube clubeMandante = clubeMandante();
        Clube clubeVisitante = clubeVisitante();

        Assertions.assertDoesNotThrow(() -> {
            partidaService.isClubesAtivos(clubeMandante, clubeVisitante);
        });
    }

    @Test
    @DisplayName("Dado um clube mandante com partida como mandante marcada menor que 48 horas de diferença, deve lançar uma exceção")
    void testIsClubeMandanteComPartidaComoMandanteMenorQue48HorasDiferenca() {
        Clube clubeMandante = clubeMandante();
        clubeMandante.setPartidasMandante(List.of(new Partida(1L, clubeMandante, clubeVisitante(), 1, 0, "1x0", estadio(), LocalDateTime.now())));
        Clube clubeVisitante = clubeVisitante();
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
        Clube clubeMandante = clubeMandante();
        clubeMandante.setPartidasVisitante(List.of(new Partida(1L, clubeMandante, clubeVisitante(), 1, 0, "1x0", estadio(), LocalDateTime.now())));
        Clube clubeVisitante = clubeVisitante();
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
        Clube clubeMandante = clubeMandante();
        Clube clubeVisitante = clubeVisitante();
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
        Clube clubeMandante = clubeMandante();
        Clube clubeVisitante = clubeVisitante();
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
        estadio.setPartidas(List.of(new Partida(1L, clubeMandante(), clubeVisitante(), 1, 0, "1x0", estadio, LocalDateTime.now())));
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

        Mockito.when(partidaRepository.existsById(idPartidaInexistente)).thenReturn(false);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            partidaService.isPartidaExiste(idPartidaInexistente);
        });

        Assertions.assertNotNull(exception);
        Mockito.verify(partidaRepository, Mockito.times(1)).existsById(idPartidaInexistente);
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
        estadio.setPartidas(List.of());
        return estadio;
    }

    private Partida partidaExistente() {
        Partida partida = new Partida();
        partida.setId(1L);
        partida.setIdClubeMandante(clubeMandante());
        partida.setIdClubeVisitante(clubeVisitante());
        partida.setGolsMandante(2);
        partida.setGolsVisitante(1);
        partida.setResultado("2x1");
        partida.setIdEstadio(estadio());
        partida.setDataHoraPartida(LocalDateTime.now());
        return partida;
    }
}