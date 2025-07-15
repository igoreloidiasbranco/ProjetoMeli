package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import br.com.meli.partidas.futebol.repository.PartidaRepository;
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

    public PartidaServiceImpl(PartidaRepository partidaRepository, ClubeRepository clubeRepository, EstadioRepository estadioRepository) {
        this.partidaRepository = partidaRepository;
        this.clubeRepository = clubeRepository;
        this.estadioRepository = estadioRepository;
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

        return partidaRepository.save(partida);
    }

    @Override
    @Transactional
    public Partida atualizarPartida(Partida partidaEditada) {

        isPartidaExiste(partidaEditada.getId());
        return partidaRepository.save(partidaEditada);
    }

    @Override
    @Transactional
    public void deletarPartida(Long id) {
        isPartidaExiste(id);
        partidaRepository.deleteById(id);
    }

    @Override
    public Partida buscarPartidaPorId(Long id) {
        isPartidaExiste(id);
        Partida partidaEncontrada = partidaRepository.getReferenceById(id);
        return partidaEncontrada;
    }

    @Override
    public Page<Partida> listarPartidas(String nomeClube, String nomeEstadio, Pageable paginacao) {

        if (nomeClube != null && !nomeClube.isEmpty()) {
            return partidaRepository.listarPartidasPorClube(nomeClube, paginacao);
        }

        if (nomeEstadio != null && !nomeEstadio.isEmpty()) {
            return partidaRepository.listarPartidasPorEstadio(nomeEstadio, paginacao);
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
    public void isPartidaExiste(Long id) {
        boolean existePartida = partidaRepository.existsById(id);
        if (!existePartida) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Partida não encontrada");
        }
    }
}