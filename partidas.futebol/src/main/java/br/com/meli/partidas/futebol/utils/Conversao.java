package br.com.meli.partidas.futebol.utils;

import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.request.EstadioRequestDTO;
import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ClubeResponseDTO;
import br.com.meli.partidas.futebol.dto.response.EstadioResponseDTO;
import br.com.meli.partidas.futebol.dto.response.PartidaResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.beans.BeanUtils;

public class Conversao {

    private Conversao() {
    }

    public static Clube dtoToEntity(ClubeRequestDTO clubeRequestDTO) {
        Clube clube = new Clube();
        BeanUtils.copyProperties(clubeRequestDTO, clube);
        return clube;
    }

    public static ClubeResponseDTO entityToDTO(Clube clube) {
        ClubeResponseDTO clubeResponseDTO = new ClubeResponseDTO();
        BeanUtils.copyProperties(clube, clubeResponseDTO);
        return clubeResponseDTO;
    }

    public static Estadio  dtoToEntity(EstadioRequestDTO estadioRequestDTO) {
        Estadio estadio = new Estadio();
        BeanUtils.copyProperties(estadioRequestDTO, estadio);
        return estadio;
    }

    public static EstadioResponseDTO entityToDTO(Estadio estadio) {
        EstadioResponseDTO estadioResponseDTO = new EstadioResponseDTO();
        BeanUtils.copyProperties(estadio, estadioResponseDTO);
        return estadioResponseDTO;
    }

    public static Partida dtoToEntity(PartidaRequestDTO partidaRequestDTO, Clube clubeMandante, Clube clubeVisitante, Estadio estadio) {
        Partida partida = new Partida();
        BeanUtils.copyProperties(partidaRequestDTO, partida);
        partida.setIdClubeMandante(clubeMandante);
        partida.setIdClubeVisitante(clubeVisitante);
        partida.setIdEstadio(estadio);
        partida.setResultado(
                clubeMandante.getNome() + " " + partida.getGolsMandante()
                        + " x " + partida.getGolsVisitante() + " "
                        + clubeVisitante.getNome());

        return partida;
    }

    public static PartidaResponseDTO entityToDTO(Partida partida) {
        PartidaResponseDTO partidaResponseDTO = new PartidaResponseDTO();
        BeanUtils.copyProperties(partida, partidaResponseDTO);
        partidaResponseDTO.setIdClubeMandante(partida.getIdClubeMandante().getId());
        partidaResponseDTO.setIdClubeVisitante(partida.getIdClubeVisitante().getId());
        partidaResponseDTO.setIdEstadio(partida.getIdEstadio().getId());
        return partidaResponseDTO;
    }
}
