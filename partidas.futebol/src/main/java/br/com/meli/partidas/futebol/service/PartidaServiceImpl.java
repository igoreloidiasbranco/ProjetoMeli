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

import java.time.LocalDate;

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
        isTimesIguais(partidaRequestDTO);
        isGolsNegativos(partidaRequestDTO.getGolsMandante(), partidaRequestDTO.getGolsVisitante());
        isDataHoraAntesCriacaoClube(partidaRequestDTO);
        Partida partida = criarPartida(partidaRequestDTO);
        return partidaRepository.save(partida);
    }

    @Override
    public void isTimesIguais(PartidaRequestDTO partidaRequestDTO) {
        if (partidaRequestDTO.getIdClubeMandante().equals(partidaRequestDTO.getIdClubeVisitante())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Clube mandante e visitante não podem ser iguais");
        }
    }

    @Override
    public void isGolsNegativos(Integer golsMandante, Integer golsVisitante) {
        if (golsMandante < 0 || golsVisitante < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gols não podem ser negativos");
        }
    }

    @Override
    public Partida criarPartida(PartidaRequestDTO partidaRequestDTO) {

        Clube clubeMandante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeMandante());

        Clube clubeVisitante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeVisitante());

        Estadio estadio = estadioRepository.findById(partidaRequestDTO.getIdEstadio())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estádio não encontrado"));

        Partida partida = Conversao.dtoToEntity(partidaRequestDTO);
        partida.setIdClubeMandante(clubeMandante);
        partida.setIdClubeVisitante(clubeVisitante);
        partida.setIdEstadio(estadio);

        partida.setResultado(
                clubeMandante.getNome() + " " + partida.getGolsMandante()
                        + " x " + partida.getGolsVisitante() + " "
                        + clubeVisitante.getNome());

        return partida;
    }

    @Override
    public void isDataHoraAntesCriacaoClube(PartidaRequestDTO partidaRequestDTO) {
        Clube clubeMandante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeMandante());

        LocalDate dataPartida = partidaRequestDTO.getDataHoraPartida().toLocalDate();

        if (clubeMandante.getDataCriacao().isAfter(dataPartida)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida não pode ser anterior à data de criação do clube mandante");
        }

        Clube clubeVisitante = clubeRepository.getReferenceById(partidaRequestDTO.getIdClubeVisitante());

        if (clubeVisitante.getDataCriacao().isAfter(dataPartida)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data da partida não pode ser anterior à data de criação do clube visitante");
        }
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
}
