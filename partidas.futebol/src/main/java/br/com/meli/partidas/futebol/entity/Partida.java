package br.com.meli.partidas.futebol.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "id_clube_mandante", nullable = false)
    private Clube idClubeMandante;

    @ManyToOne
    @JoinColumn(name = "id_clube_visitante", nullable = false)
    private Clube idClubeVisitante;

    private Integer golsMandante;

    private Integer golsVisitante;

    private String resultado;

    @ManyToOne
    @JoinColumn(name = "id_estadio", nullable = false)
    private Estadio idEstadio;

    private LocalDateTime dataHoraPartida;


    public Partida(Long id, Clube idClubeMandante, Clube idClubeVisitante, Integer golsMandante, Integer golsVisitante, String resultado, Estadio idEstadio, LocalDateTime dataHoraPartida) {
        this.id = id;
        this.idClubeMandante = idClubeMandante;
        this.idClubeVisitante = idClubeVisitante;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
        this.resultado = resultado;
        this.idEstadio = idEstadio;
        this.dataHoraPartida = dataHoraPartida;
    }

    public Partida() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clube getIdClubeMandante() {
        return idClubeMandante;
    }

    public void setIdClubeMandante(Clube idClubeMandante) {
        this.idClubeMandante = idClubeMandante;
    }

    public Clube getIdClubeVisitante() {
        return idClubeVisitante;
    }

    public void setIdClubeVisitante(Clube idClubeVisitante) {
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

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Estadio getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(Estadio idEstadio) {
        this.idEstadio = idEstadio;
    }

    public LocalDateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
    }
}
