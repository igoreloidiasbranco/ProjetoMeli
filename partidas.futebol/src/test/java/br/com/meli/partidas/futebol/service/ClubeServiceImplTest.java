package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.exception.IdNotFoundException;
import br.com.meli.partidas.futebol.exception.NomeAndSiglaExistsException;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static br.com.meli.partidas.futebol.enums.Sigla.RJ;
import static br.com.meli.partidas.futebol.enums.Sigla.SP;

class ClubeServiceImplTest {

    ClubeService clubeService;
    ClubeRepository clubeRepository;

    @BeforeEach
    void setUp() {
        clubeRepository = Mockito.mock(ClubeRepository.class);
        clubeService = new ClubeServiceImpl(clubeRepository);
    }

    @Test
    @DisplayName("Dado um clube válido, deve salvar o clube com sucesso")
    void testeSalvarClube() {
        Clube clubeValido = clubeValido();
        Clube clubeSalvoNoBanco = clubeSalvoNoBanco();

        Mockito.when(clubeRepository.save(clubeValido)).thenReturn(clubeSalvoNoBanco);
        Clube resultado = clubeService.salvarClube(clubeValido);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(clubeSalvoNoBanco.getNome(), resultado.getNome());
        Assertions.assertEquals(clubeSalvoNoBanco.getSigla(), resultado.getSigla());
        Assertions.assertEquals(clubeSalvoNoBanco.getDataCriacao(), resultado.getDataCriacao());
        Assertions.assertEquals(clubeSalvoNoBanco.getAtivo(), resultado.getAtivo());
        Mockito.verify(clubeRepository, Mockito.times(1)).save(clubeValido);
    }

    @Test
    @DisplayName("Dado um clube válido, deve atualizar o clube com sucesso")
    void testeAtualizarClube() {
        Clube clubeSalvoNoBanco = clubeSalvoNoBanco();

        Clube clubeAlterado = clubeValido();
        clubeAlterado.setId(clubeSalvoNoBanco().getId());
        clubeAlterado.setNome("Nome do clube editado");
        clubeAlterado.setSigla(RJ);
        clubeAlterado.setDataCriacao(LocalDate.of(1935, 1, 25));

        Mockito.when(clubeRepository.existsById(clubeAlterado.getId())).thenReturn(true);
        Mockito.when(clubeRepository.findById(clubeAlterado.getId())).thenReturn(Optional.of(clubeSalvoNoBanco));
        Mockito.when(clubeRepository.save(Mockito.any(Clube.class))).thenReturn(clubeAlterado);

        Clube resultado = clubeService.atualizarClube(clubeAlterado);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(clubeAlterado.getNome(), resultado.getNome());
        Assertions.assertEquals(clubeAlterado.getSigla(), resultado.getSigla());
        Assertions.assertEquals(clubeAlterado.getDataCriacao(), resultado.getDataCriacao());
        Assertions.assertEquals(clubeAlterado.getAtivo(), resultado.getAtivo());
    }

    @Test
    @DisplayName("Dado um clube existente, deve inativar o clube com sucesso")
    void testeInativarClube() {
        
        Clube clubeExistente = clubeSalvoNoBanco();

        Mockito.when(clubeRepository.existsById(clubeExistente.getId())).thenReturn(true);
        Mockito.when(clubeRepository.getReferenceById(clubeExistente.getId())).thenReturn(clubeExistente);

        clubeService.inativarClube(clubeExistente.getId());

        Assertions.assertNotNull(clubeExistente);
        Assertions.assertFalse(clubeExistente.getAtivo());
    }

    @Test
    @DisplayName("Dado um id existente, deve retornar o clube com sucesso")
    void testeBuscarClubePorId() {

        Long idExistente = 1L;

        Mockito.when(clubeRepository.existsById(idExistente)).thenReturn(true);
        Mockito.when(clubeRepository.getReferenceById(idExistente)).thenReturn(clubeSalvoNoBanco());

        Clube resultado = clubeService.buscarClubePorId(idExistente);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(idExistente, resultado.getId());

    }

    @Test
    @DisplayName("Deve retornar todos os clubes com sucesso")
    void testeListarClubes() {

        List<Clube> clubesSalvos = clubesSalvosNoBanco();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Clube> page = new PageImpl<>(clubesSalvos, pageable, clubesSalvos.size());

        Mockito.when(clubeRepository.findAll(pageable)).thenReturn(page);
        Page<Clube> resultado = clubeService.listarClubes(null, null, null, pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.getTotalElements());
        Assertions.assertEquals(1, resultado.getTotalPages());
        Assertions.assertEquals(resultado.getContent().contains(clubesSalvos.get(0)), true);
        Assertions.assertEquals(resultado.getContent().contains(clubesSalvos.get(1)), true);
    }

    @Test
    @DisplayName("Deve retornar todos os clubes filtrados por nome com sucesso")
    void testeListarClubesFiltradosPorNome() {

        List<Clube> clubesSalvos = clubesSalvosNoBanco();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Clube> page = new PageImpl<>(List.of(clubesSalvos.get(0)), pageable, 1);

        Mockito.when(clubeRepository.findByNome(clubesSalvos.get(0).getNome(), pageable)).thenReturn(page);
        Page<Clube> resultado = clubeService.listarClubes("Nome do clube1", null, null, pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getTotalElements());
        Assertions.assertEquals(1, resultado.getTotalPages());
        Assertions.assertEquals(resultado.getContent().contains(clubesSalvos.get(0)), true);
        Assertions.assertEquals(resultado.getContent().contains(clubesSalvos.get(1)), false);
        Assertions.assertEquals("Nome do clube1", resultado.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar todos os clubes filtrados por sigla com sucesso")
    void testeListarClubesFiltradosPorSigla() {

        List<Clube> clubesSalvos = clubesSalvosNoBanco();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Clube> page = new PageImpl<>(List.of(clubesSalvos.get(1)), pageable, 1);

        Mockito.when(clubeRepository.findBySigla(clubesSalvos.get(1).getSigla(), pageable)).thenReturn(page);
        Page<Clube> resultado = clubeService.listarClubes(null, RJ, null, pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getTotalElements());
        Assertions.assertEquals(1, resultado.getTotalPages());
        Assertions.assertEquals(resultado.getContent().contains(clubesSalvos.get(0)), false);
        Assertions.assertEquals(resultado.getContent().contains(clubesSalvos.get(1)), true);
        Assertions.assertEquals(RJ, resultado.getContent().get(0).getSigla());
    }

    @Test
    @DisplayName("Deve retornar todos os clubes filtrados por ativo com sucesso")
    void testeListarClubesFiltradosPorAtivo() {

        List<Clube> clubesSalvos = clubesSalvosNoBanco();
        List<Clube> clubesAtivos = clubesSalvos.stream().filter(Clube::getAtivo).toList();
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Clube> page = new PageImpl<>(clubesAtivos, pageable, clubesAtivos.size());

        Mockito.when(clubeRepository.findByAtivo(true, pageable)).thenReturn(page);
        Page<Clube> resultado = clubeService.listarClubes(null, null, true, pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.getTotalElements());
        Assertions.assertEquals(1, resultado.getTotalPages());
        Assertions.assertEquals(resultado.getContent().contains(clubesAtivos.get(0)), true);
        Assertions.assertEquals(resultado.getContent().contains(clubesAtivos.get(1)), true);
        Assertions.assertEquals(true, resultado.getContent().get(0).getAtivo());
        Assertions.assertEquals(true, resultado.getContent().get(1).getAtivo());
    }

    @Test
    @DisplayName("Dado que já existe um clube com esse nome nesta sigla, deve lançar uma exceção NomeAndSiglaExistsException")
    void testeIsExisteNomeNestaSigla() {
        List<Clube> clubes = clubesSalvosNoBanco();
        Clube clubeAlteradoComMesmoNomeSigla = clubes.get(1);
        clubeAlteradoComMesmoNomeSigla.setNome(clubes.get(0).getNome());
        clubeAlteradoComMesmoNomeSigla.setSigla(clubes.get(0).getSigla());

        Mockito.when(clubeRepository.findByNomeAndSigla(clubeAlteradoComMesmoNomeSigla.getNome(), clubeAlteradoComMesmoNomeSigla.getSigla())).thenReturn(clubes.get(0));

        NomeAndSiglaExistsException exception = Assertions.assertThrows(
                NomeAndSiglaExistsException.class, () -> clubeService.isExisteNomeNestaSigla(clubeAlteradoComMesmoNomeSigla));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Já existe um clube com esse nome nesta sigla", exception.getMessage());
    }

    @Test
    @DisplayName("Dado um id existente, não deve lançar uma exceção IdNotFoundException")
    void testeIsClubeExiste_com_id_existente() {
        Long idExistente = 1L;

        Mockito.when(clubeRepository.existsById(idExistente)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> clubeService.isClubeExiste(idExistente));

    }

    @Test
    @DisplayName("Dado um id inexistente, deve lançar uma exceção IdNotFoundException")
    void testeIsClubeExiste_com_id_inexistente() {
        Long idInexistente = 2L;

        Mockito.when(clubeRepository.existsById(idInexistente)).thenReturn(false);

        IdNotFoundException exception = Assertions.assertThrows(
                IdNotFoundException.class, () -> clubeService.isClubeExiste(idInexistente));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Id não encontrado", exception.getMessage());

    }

    @Test
    @DisplayName("Dado um clube com com data de criação maior que a data da partida registrada, deve lançar uma exceção")
    void testeIsDataCriacaoMenorQueDataPartidas() {
        List<Clube> clubes = clubesSalvosNoBanco();
        Estadio estadio = new Estadio();
        estadio.setId(1L);
        estadio.setNome("Nome do estádio");
        estadio.setSigla(SP);
        Partida partida = new Partida();
        partida.setId(1L);
        partida.setIdClubeMandante(clubes.get(0));
        partida.setIdClubeVisitante(clubes.get(1));
        partida.setGolsMandante(1);
        partida.setGolsVisitante(0);
        partida.setIdEstadio(estadio);
        partida.setDataHoraPartida(LocalDateTime.now());
        clubes.get(0).setPartidasMandante(List.of(partida));
        clubes.get(0).setPartidasVisitante(List.of());
        Clube clubeAlteradoComDataCriacaoMaiorQueDataPartida = clubes.get(0);
        clubeAlteradoComDataCriacaoMaiorQueDataPartida.setDataCriacao(LocalDate.now().plusDays(1));

        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class, () ->
                        clubeService.isDataCriacaoMenorQueDataPartidas(
                                clubeAlteradoComDataCriacaoMaiorQueDataPartida));

        Assertions.assertNotNull(exception);
        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    private Clube clubeValido() {
        Clube clube = new Clube();
        clube.setNome("Nome do clube");
        clube.setSigla(SP);
        clube.setDataCriacao(LocalDate.of(1930, 1, 25));
        clube.setAtivo(true);
        return clube;
    }

    private Clube clubeSalvoNoBanco() {
        Clube clube = new Clube();
        clube.setId(1L);
        clube.setNome("Nome do clube");
        clube.setSigla(SP);
        clube.setDataCriacao(LocalDate.of(1930, 1, 25));
        clube.setAtivo(true);
        return clube;
    }

    private List<Clube> clubesSalvosNoBanco() {
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

        return List.of(clube1, clube2);
    }
}