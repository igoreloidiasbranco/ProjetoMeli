package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import br.com.meli.partidas.futebol.repository.PartidaRepository;
import br.com.meli.partidas.futebol.utils.Conversao;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

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
    public Partida salvarPartida(PartidaRequestDTO partidaRequestDTO) {
        isClubesExistem(partidaRequestDTO);
        isClubesDiferentes(partidaRequestDTO);
        isGolsPositivos(partidaRequestDTO.getGolsMandante(), partidaRequestDTO.getGolsVisitante());
        Clube clubeMandante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeMandante());
        Clube clubeVisitante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeVisitante());
        isDataHoraAntesCriacaoClube(clubeMandante,clubeVisitante, partidaRequestDTO.getDataHoraPartida().toLocalDate());
        isClubesAtivos(clubeMandante, clubeVisitante);
        isPartidaAposIntervalo(clubeMandante, clubeVisitante, partidaRequestDTO.getDataHoraPartida());
        Estadio estadio = buscarEstadio(partidaRequestDTO.getIdEstadio());
        Partida partida = Conversao.dtoToEntity(partidaRequestDTO, clubeMandante, clubeVisitante, estadio);
        return partidaRepository.save(partida);
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
        }
        else if (clubeVisitante.getDataCriacao().isAfter(dataPartida)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida não pode ser anterior à data de criação do clube visitante");
        }
    }

    @Override
    public void isClubesAtivos(Clube clubeMandante, Clube clubeVisitante) {

        if(clubeMandante.getAtivo() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube mandante não está ativo");
        }
        else if(clubeVisitante.getAtivo() == false) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clube visitante não está ativo");
        }
    }

    private void isPartidaAposIntervalo(Clube clubeMandante, Clube clubeVisitante, LocalDateTime dataHoraPartida) {

        final int HORAS_INTERVALO = 48;
        boolean existePartidaMenos48h = Stream.concat(
                        Stream.concat(clubeMandante.getPartidasMandante().stream(), clubeMandante.getPartidasVisitante().stream()),
                        Stream.concat(clubeVisitante.getPartidasMandante().stream(), clubeVisitante.getPartidasVisitante().stream())
                )
                .anyMatch(p -> Duration.between(p.getDataHoraPartida(), dataHoraPartida).toHours() < HORAS_INTERVALO);
        if(existePartidaMenos48h) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Um dos clubes envolvidos já possue outra partida marcada" +
                    " com diferença menor do que "+ HORAS_INTERVALO + " horas em relação a esta");
        }

    }


    @Override
    public Estadio buscarEstadio(Long idEstadio) {
        return estadioRepository.findById(idEstadio)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estádio não encontrado"));
    }

}
