package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface PartidaService {

    void isClubesExistem(PartidaRequestDTO partidaRequestDTO);
    void isClubesDiferentes(PartidaRequestDTO partidaRequestDTO);
    void isGolsPositivos(Integer golsMandante, Integer golsVisitante);
    void isDataHoraAntesCriacaoClube(Clube clubeMandante, Clube clubeVisitante, LocalDate dataHoraPartida);
    void isClubesAtivos(Clube clubeMandante, Clube clubeVisitante);
    void isPartidaAposIntervalo(Clube clubeMandante, Clube clubeVisitante, LocalDateTime dataHoraPartida);
    Estadio buscarEstadio(Long idEstadio);
    void isEstadioSemPartida(Estadio estadio, LocalDate dataPartida);
    Partida salvarPartida(PartidaRequestDTO partidaRequestDTO);

}
