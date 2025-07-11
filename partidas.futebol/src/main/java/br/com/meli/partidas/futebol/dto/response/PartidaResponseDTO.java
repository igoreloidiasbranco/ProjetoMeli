package br.com.meli.partidas.futebol.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class PartidaResponseDTO {

    private Long id;

    @JsonProperty("id_clube_mandante")
    private Long idClubeMandante;

    @JsonProperty("id_clube_visitante")
    private Long idClubeVisitante;

    private String resultado;

    @JsonProperty("id_estadio")
    private Long idEstadio;

    private LocalDateTime dataHoraPartida;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
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
