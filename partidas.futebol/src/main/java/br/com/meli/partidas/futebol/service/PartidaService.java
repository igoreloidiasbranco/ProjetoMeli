package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.RetrospectoDoClubeContraAdversarioResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PartidaService {

    Partida validarPartida(PartidaRequestDTO partidaRequestDTO, Long id);
    Partida salvarPartida(Partida partida);
    Partida atualizarPartida(Partida partidaEditada);
    void deletarPartida(Long id);
    Partida buscarPartidaPorId(Long id);
    Page<Partida> listarPartidas(String nomeClube, String nomeEstadio, Pageable paginacao);
    void isClubesExistem(PartidaRequestDTO partidaRequestDTO);
    void isClubesDiferentes(PartidaRequestDTO partidaRequestDTO);
    void isGolsPositivos(Integer golsMandante, Integer golsVisitante);
    void isDataHoraAntesCriacaoClube(Clube clubeMandante, Clube clubeVisitante, LocalDate dataHoraPartida);
    void isClubesAtivos(Clube clubeMandante, Clube clubeVisitante);
    void isPartidaAposIntervalo(Clube clubeMandante, Clube clubeVisitante, LocalDateTime dataHoraPartida, Long idPartidaAtual);
    Estadio buscarEstadio(Long idEstadio);
    void isEstadioSemPartida(Estadio estadio, LocalDate dataPartida, Long idPartidaAtual);
    void calcularEstatisticasDosClubes(Partida partida);
    void removerPartidaDesatualizadaNosClubes(Partida partidaDesatualizada);
    RetrospectoDoClubeContraAdversarioResponseDTO buscarRetrospectoDoClubeContraAdversario(Long idClube, Long idAdversario);
}
