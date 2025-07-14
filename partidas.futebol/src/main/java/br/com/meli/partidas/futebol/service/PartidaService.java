package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;

import java.time.LocalDate;

public interface PartidaService {

    void isClubesExistem(PartidaRequestDTO partidaRequestDTO);
    void isClubesDiferentes(PartidaRequestDTO partidaRequestDTO);
    void isGolsPositivos(Integer golsMandante, Integer golsVisitante);
    void isDataHoraAntesCriacaoClube(Clube clubeMandante, Clube clubeVisitante, LocalDate dataHoraPartida);
    void isClubesAtivos(Clube clubeMandante, Clube clubeVisitante);
    Estadio buscarEstadio(Long idEstadio);
    Partida salvarPartida(PartidaRequestDTO partidaRequestDTO);

}
