package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ConfrontoDiretoResponseDTO;
import br.com.meli.partidas.futebol.dto.response.RetrospectoDoClubeContraOutroResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import br.com.meli.partidas.futebol.repository.PartidaRepository;
import br.com.meli.partidas.futebol.utils.Builder;
import br.com.meli.partidas.futebol.utils.Conversao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class PartidaServiceImpl implements PartidaService {

    private final ClubeRepository clubeRepository;
    private final PartidaRepository partidaRepository;
    private final EstadioRepository estadioRepository;
    private final ClubeService clubeService;

    public PartidaServiceImpl(PartidaRepository partidaRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository, ClubeService clubeService) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
        this.clubeService = clubeService;
    }

    @Override
    public Partida validarPartida(PartidaRequestDTO partidaRequestDTO, Long idPartidaAtual) {
        isClubesExistem(partidaRequestDTO);
        isClubesDiferentes(partidaRequestDTO);
        isGolsPositivos(partidaRequestDTO.getGolsMandante(), partidaRequestDTO.getGolsVisitante());
        Clube clubeMandante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeMandante());
        Clube clubeVisitante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeVisitante());
        isDataHoraAntesCriacaoClube(clubeMandante, clubeVisitante, partidaRequestDTO.getDataHoraPartida().toLocalDate());
        isClubesAtivos(clubeMandante, clubeVisitante);
        isPartidaAposIntervalo(clubeMandante, clubeVisitante, partidaRequestDTO.getDataHoraPartida(), idPartidaAtual);
        Estadio estadio = buscarEstadio(partidaRequestDTO.getIdEstadio());
        isEstadioSemPartida(estadio, partidaRequestDTO.getDataHoraPartida().toLocalDate(), idPartidaAtual);
        Partida partida = Conversao.dtoToEntity(partidaRequestDTO, clubeMandante, clubeVisitante, estadio);
        return partida;
    }

    @Override
    @Transactional
    public Partida salvarPartida(Partida partida) {

        partidaRepository.save(partida);
        calcularEstatisticasDosClubes(partida);
        return partida;
    }

    @Override
    @Transactional
    public Partida atualizarPartida(Partida partidaEditada) {

        Partida partidaDesatualizada = buscarPartidaPorId(partidaEditada.getId());

        removerPartidaDesatualizadaNosClubes(partidaDesatualizada);

        partidaRepository.save(partidaEditada);

        calcularEstatisticasDosClubes(partidaEditada);


        return partidaEditada;
    }

    @Override
    @Transactional
    public void deletarPartida(Long id) {

        Partida partida = buscarPartidaPorId(id);
        removerPartidaDesatualizadaNosClubes(partida);
        partidaRepository.delete(partida);
    }

    @Override
    public Partida buscarPartidaPorId(Long id) {

        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada"));
    }

    @Override
    public Page<Partida> listarPartidas(String nomeClube, String nomeEstadio, Integer goleadasComDiferencaGols, Pageable paginacao) {

        if (nomeClube != null && !nomeClube.isEmpty()) {
            return partidaRepository.listarPartidasPorClube(nomeClube, paginacao);
        }

        if (nomeEstadio != null && !nomeEstadio.isEmpty()) {
            return partidaRepository.listarPartidasPorEstadio(nomeEstadio, paginacao);
        }

        if (goleadasComDiferencaGols != null) {
            if (goleadasComDiferencaGols < 3) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Goleadas devem ser maiores ou iguais a 3");
            }
            return partidaRepository.listarPartidasPorGoleadas(goleadasComDiferencaGols, paginacao);
        }

        return partidaRepository.findAll(paginacao);
    }


    @Override
    public void isClubesExistem(PartidaRequestDTO partidaRequestDTO) {

        boolean isClubeExiste = clubeRepository.existsById(partidaRequestDTO.getIdClubeMandante());

        if (!isClubeExiste) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube mandante não encontrado");
        }

        isClubeExiste = clubeRepository.existsById(partidaRequestDTO.getIdClubeVisitante());
        if (!isClubeExiste) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube visitante não encontrado");
        }
    }

    @Override
    public void isClubesDiferentes(PartidaRequestDTO partidaRequestDTO) {
        if (partidaRequestDTO.getIdClubeMandante().equals(partidaRequestDTO.getIdClubeVisitante())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Clube mandante e visitante não podem ser iguais");
        }
    }

    @Override
    public void isClubesDiferentes(Long idClube, Long idAdversario) {
        if (idClube.equals(idAdversario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Clubes não podem ser iguais");
        }
    }

    @Override
    public void isGolsPositivos(Integer golsMandante, Integer golsVisitante) {
        if (golsMandante < 0 || golsVisitante < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols não podem ser negativos");
        }
    }

    @Override
    public void isDataHoraAntesCriacaoClube(Clube clubeMandante, Clube clubeVisitante, LocalDate dataPartida) {

        if (clubeMandante.getDataCriacao().isAfter(dataPartida)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida não pode ser anterior à data de criação do clube mandante");
        } else if (clubeVisitante.getDataCriacao().isAfter(dataPartida)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida não pode ser anterior à data de criação do clube visitante");
        }
    }

    @Override
    public void isClubesAtivos(Clube clubeMandante, Clube clubeVisitante) {

        if (clubeMandante.getAtivo() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube mandante não está ativo");
        } else if (clubeVisitante.getAtivo() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube visitante não está ativo");
        }
    }

    @Override
    public void isPartidaAposIntervalo(Clube clubeMandante, Clube clubeVisitante, LocalDateTime dataHoraPartida, Long idPartidaAtual) {

        final int HORAS_INTERVALO = 48;

        List<Partida> partidasGeralMandante = new ArrayList<>();

        if (clubeMandante.getPartidasMandante() != null && !clubeMandante.getPartidasMandante().isEmpty()) {
            partidasGeralMandante.addAll(clubeMandante.getPartidasMandante());
        }
        if (clubeMandante.getPartidasVisitante() != null && !clubeMandante.getPartidasVisitante().isEmpty()) {
            partidasGeralMandante.addAll(clubeMandante.getPartidasVisitante());
        }
        for (Partida partida : partidasGeralMandante) {
            if (partida.getId().equals(idPartidaAtual)) {
                continue;
            }
            if (Math.abs(Duration.between(partida.getDataHoraPartida(), dataHoraPartida).toHours()) < HORAS_INTERVALO) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube mandante já possue outra partida cadastrada" +
                        " com diferença menor do que " + HORAS_INTERVALO + " horas em relação a esta");
            }
        }

        List<Partida> partidasGeralVisitante = new ArrayList<>();

        if (clubeVisitante.getPartidasMandante() != null && !clubeVisitante.getPartidasMandante().isEmpty()) {
            partidasGeralVisitante.addAll(clubeVisitante.getPartidasMandante());
        }
        if (clubeVisitante.getPartidasVisitante() != null && !clubeVisitante.getPartidasVisitante().isEmpty()) {
            partidasGeralVisitante.addAll(clubeVisitante.getPartidasVisitante());
        }
        for (Partida partida : partidasGeralVisitante) {
            if (partida.getId().equals(idPartidaAtual)) {
                continue;
            }
            if (Math.abs(Duration.between(partida.getDataHoraPartida(), dataHoraPartida).toHours()) < HORAS_INTERVALO) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube visitante já possue outra partida cadastrada" +
                        " com diferença menor do que " + HORAS_INTERVALO + " horas em relação a esta");
            }
        }
    }

    @Override
    public void isEstadioSemPartida(Estadio estadio, LocalDate dataPartida, Long idPartidaAtual) {
        boolean existePartidaNoEstadio = estadio.getPartidas().stream()
                .filter(partida -> !partida.getId().equals(idPartidaAtual))
                .anyMatch(partida -> partida.getDataHoraPartida().toLocalDate().equals(dataPartida));
        if (existePartidaNoEstadio) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Estádio já possui partida marcada para esta data");
        }
    }

    @Override
    public Estadio buscarEstadio(Long idEstadio) {
        return estadioRepository.findById(idEstadio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estádio não encontrado"));
    }

    @Override
    public Clube buscarClube(Long idClube) {
        return clubeRepository.findById(idClube)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clube não encontrado"));
    }


    @Override
    @Transactional
    public void calcularEstatisticasDosClubes(Partida partida) {
        Clube clubeMandante = buscarClube(partida.getIdClubeMandante().getId());
        Clube clubeVisitante = buscarClube(partida.getIdClubeVisitante().getId());

        clubeMandante.getPartidasMandante().add(partida);
        clubeVisitante.getPartidasVisitante().add(partida);

        clubeService.calcularEstatisticas(clubeMandante);
        clubeService.calcularEstatisticas(clubeVisitante);
    }


    @Override
    @Transactional
    public void removerPartidaDesatualizadaNosClubes(Partida partidaDesatualizada) {
        Clube clubeMandanteAntigo = buscarClube(partidaDesatualizada.getIdClubeMandante().getId());
        Clube clubeVisitanteAntigo = buscarClube(partidaDesatualizada.getIdClubeVisitante().getId());

        clubeMandanteAntigo.getPartidasMandante().removeIf(p -> p.getId().equals(partidaDesatualizada.getId()));
        clubeVisitanteAntigo.getPartidasVisitante().removeIf(p -> p.getId().equals(partidaDesatualizada.getId()));

        clubeService.calcularEstatisticas(clubeMandanteAntigo);
        clubeService.calcularEstatisticas(clubeVisitanteAntigo);
    }

    @Override
    public List<Partida> buscarPartidasEntreClubes(Clube clubeUm, Clube clubeDois) {
        return partidaRepository.findByIdClubeMandanteAndIdClubeVisitante(clubeUm, clubeDois);
    }

    @Override
    public RetrospectoDoClubeContraOutroResponseDTO buscarRetrospectoDoClubeContraOutro(Long idClubeUm, Long idClubeDois) {
        isClubesDiferentes(idClubeUm, idClubeDois);
        Clube clube = buscarClube(idClubeUm);
        Clube adversario = buscarClube(idClubeDois);
        List<Partida> partidasComoMandante = buscarPartidasEntreClubes(clube, adversario);
        List<Partida> partidasComoVisitante = buscarPartidasEntreClubes(adversario, clube);

        if (partidasComoMandante.isEmpty() && partidasComoVisitante.isEmpty()) {
            return Builder.retrospectoDoClubeContraOutroResponseDTOVazio(clube, adversario);
        }

        RetrospectoDoClubeContraOutroResponseDTO retrospectoComoMandante = calcularRetrospectoClubeContraOutro(partidasComoMandante);
        RetrospectoDoClubeContraOutroResponseDTO retrospectoComoVisitante = calcularRetrospectoClubeContraOutro(partidasComoVisitante);

        return new RetrospectoDoClubeContraOutroResponseDTO()
                .setNomeClube(clube.getNome())
                .setNomeAdversario(adversario.getNome())
                .setVitorias(retrospectoComoMandante.getVitorias() + retrospectoComoVisitante.getDerrotas())
                .setEmpates(retrospectoComoMandante.getEmpates() + retrospectoComoVisitante.getEmpates())
                .setDerrotas(retrospectoComoMandante.getDerrotas() + retrospectoComoVisitante.getVitorias())
                .setGolsMarcados(retrospectoComoMandante.getGolsMarcados() + retrospectoComoVisitante.getGolsSofridos())
                .setGolsSofridos(retrospectoComoMandante.getGolsSofridos() + retrospectoComoVisitante.getGolsMarcados());
    }

    @Override
    public RetrospectoDoClubeContraOutroResponseDTO calcularRetrospectoClubeContraOutro(List<Partida> partidas) {
        RetrospectoDoClubeContraOutroResponseDTO retrospecto = new RetrospectoDoClubeContraOutroResponseDTO();
        int vitorias = 0, empates = 0, derrotas = 0, golsMarcados = 0, golsSofridos = 0;

        for (Partida partida : partidas) {
            if (partida.getGolsMandante() > partida.getGolsVisitante()) {
                vitorias++;
            } else if (partida.getGolsMandante().equals(partida.getGolsVisitante())) {
                empates++;
            } else {
                derrotas++;
            }
            golsMarcados += partida.getGolsMandante();
            golsSofridos += partida.getGolsVisitante();
        }

        retrospecto.setVitorias(vitorias)
                .setEmpates(empates)
                .setDerrotas(derrotas)
                .setGolsMarcados(golsMarcados)
                .setGolsSofridos(golsSofridos);

        return retrospecto;
    }

    @Override
    public ConfrontoDiretoResponseDTO buscarConfrontoDireto(Long idClubeUm, Long idClubeDois) {
        Clube clubeUm = buscarClube(idClubeUm);
        Clube clubeDois = buscarClube(idClubeDois);
        List<Partida> confrontosDiretos = new ArrayList<>();
        confrontosDiretos.addAll(partidaRepository.findByIdClubeMandanteAndIdClubeVisitante(clubeUm, clubeDois));
        confrontosDiretos.addAll(partidaRepository.findByIdClubeMandanteAndIdClubeVisitante(clubeDois, clubeUm));

        RetrospectoDoClubeContraOutroResponseDTO retrospectoUm = buscarRetrospectoDoClubeContraOutro(idClubeUm, idClubeDois);
        RetrospectoDoClubeContraOutroResponseDTO retrospectoDois = buscarRetrospectoDoClubeContraOutro(idClubeDois, idClubeUm);

        return new ConfrontoDiretoResponseDTO().setPartidas(Conversao.entityListToDTOList(confrontosDiretos))
                .setRetrospectoClubeUm(retrospectoUm)
                .setRetrospectoClubeDois(retrospectoDois);
    }
}