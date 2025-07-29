package br.com.meli.partidas.futebol.utils;

import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.request.EstadioRequestDTO;
import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.*;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.beans.BeanUtils;
import java.util.List;

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

    public static Clube ClubeEditadoToClubeBanco(Clube clubeEditado, Clube clubeBanco) {
        clubeBanco.setNome(clubeEditado.getNome());
        clubeBanco.setSigla(clubeEditado.getSigla());
        clubeBanco.setDataCriacao(clubeEditado.getDataCriacao());
        clubeBanco.setAtivo(clubeEditado.getAtivo());
        return clubeBanco;
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

    public static RetrospectoDoClubeResponseDTO entityToRetrospectoDTO(Clube clube) {

        RetrospectoDoClubeResponseDTO retrospecto = new RetrospectoDoClubeResponseDTO();
        retrospecto.setNomeClube(clube.getNome());
        retrospecto.setVitorias(clube.getVitorias());
        retrospecto.setEmpates(clube.getEmpates());
        retrospecto.setDerrotas(clube.getDerrotas());
        retrospecto.setGolsMarcados(clube.getGolsMarcados());
        retrospecto.setGolsSofridos(clube.getGolsSofridos());
        return retrospecto;
    }

    public static List<PartidaResponseDTO> entityListToDTOList(List<Partida> partidas) {
        return partidas.stream()
                .map(Conversao::entityToDTO)
                .toList();
    }

    public static List<RankingResponseDTO> entityToRankingResponseDTO(List<Clube> clubes) {
        return clubes.stream()
                .map(clube -> {
                    RankingResponseDTO rankingResponseDTO = new RankingResponseDTO();
                    rankingResponseDTO.setNome(clube.getNome());
                    rankingResponseDTO.setPontos(clube.getPontos());
                    rankingResponseDTO.setGolsMarcados(clube.getGolsMarcados());
                    rankingResponseDTO.setVitorias(clube.getVitorias());
                    rankingResponseDTO.setTotalPartidas(clube.getTotalPartidas());
                    return rankingResponseDTO;
                })
                .toList();
    }
}
