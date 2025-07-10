package br.com.meli.partidas.futebol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public class PartidaRequestDTO {

    @NotNull
    @JsonProperty("id_clube_mandante")
    private Long idClubeMandante;

    @NotNull
    @JsonProperty("id_clube_visitante")
    private Long idClubeVisitante;

    @NotNull
    @JsonProperty("gols_mandante")
    private Integer golsMandante;

    @NotNull
    @JsonProperty("gols_visitante")
    private Integer golsVisitante;

    @NotNull
    @JsonProperty("id_estadio")
    private Long idEstadio;

    @NotNull
    @JsonProperty("data_hora_partida")
    @PastOrPresent
    private LocalDateTime dataHoraPartida;



    public Long getIdClubeMandante() {
        return idClubeMandante;
    }

    public void setIdClubeMandante(Long idClubeMandante) {
        this.idClubeMandante = idClubeMandante;
    }

    public Long getIdClubeVisitante() {
        return idClubeVisitante;
    }

    public void setIdClubeVisitante(Long idClubeVisitante) {
        this.idClubeVisitante = idClubeVisitante;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }


    public Long getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(Long idEstadio) {
        this.idEstadio = idEstadio;
    }

    public LocalDateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
    }
}
