package br.com.meli.partidas.futebol.utils;

import br.com.meli.partidas.futebol.dto.response.RetrospectoDoClubeContraOutroResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;

public class Builder {

    private Builder() {
    }

    public static RetrospectoDoClubeContraOutroResponseDTO retrospectoDoClubeContraOutroResponseDTOVazio(Clube clubeUm, Clube clubeDois) {
        return new RetrospectoDoClubeContraOutroResponseDTO()
                .setNomeClube(clubeUm.getNome())
                .setNomeAdversario(clubeDois.getNome())
                .setVitorias(0)
                .setEmpates(0)
                .setDerrotas(0)
                .setGolsMarcados(0)
                .setGolsSofridos(0);
    }
}
