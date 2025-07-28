package br.com.meli.partidas.futebol.dto.response;

import java.util.List;

public class ConfrontoDiretoResponseDTO {

    private List<PartidaResponseDTO> partidas;
    RetrospectoDoClubeContraOutroResponseDTO retrospectoClubeUm;
    RetrospectoDoClubeContraOutroResponseDTO retrospectoClubeDois;

    public List<PartidaResponseDTO> getPartidas() {
        return partidas;
    }

    public RetrospectoDoClubeContraOutroResponseDTO getRetrospectoClubeUm() {
        return retrospectoClubeUm;
    }

    public RetrospectoDoClubeContraOutroResponseDTO getRetrospectoClubeDois() {
        return retrospectoClubeDois;
    }

    public ConfrontoDiretoResponseDTO setPartidas(List<PartidaResponseDTO> partidas) {
        this.partidas = partidas;
        return this;
    }

    public ConfrontoDiretoResponseDTO setRetrospectoClubeUm(RetrospectoDoClubeContraOutroResponseDTO retrospectoClubeUm) {
        this.retrospectoClubeUm = retrospectoClubeUm;
        return this;
    }

    public ConfrontoDiretoResponseDTO setRetrospectoClubeDois(RetrospectoDoClubeContraOutroResponseDTO retrospectoClubeDois) {
        this.retrospectoClubeDois = retrospectoClubeDois;
        return this;
    }
}
