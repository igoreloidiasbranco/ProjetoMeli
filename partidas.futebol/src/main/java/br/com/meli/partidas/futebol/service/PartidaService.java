package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.entity.Partida;

public interface PartidaService {

    void isTimesIguais(PartidaRequestDTO partidaRequestDTO);
    void isGolsNegativos(Integer golsMandante, Integer golsVisitante);
    Partida criarPartida(PartidaRequestDTO partidaRequestDTO);
    Partida salvarPartida(PartidaRequestDTO partidaRequestDTO);

}
