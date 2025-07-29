package br.com.meli.partidas.futebol.entity;

import br.com.meli.partidas.futebol.enums.Sigla;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "clubes")
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Sigla sigla;


    private LocalDate dataCriacao;

    private boolean ativo;

    private Integer vitorias = 0;

    private Integer empates = 0;

    private Integer derrotas = 0;

    private Integer golsMarcados = 0;

    private Integer golsSofridos = 0;

    private Integer pontos = 0;

    @OneToMany(mappedBy = "idClubeMandante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partida> partidasMandante;

    @OneToMany(mappedBy = "idClubeVisitante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partida> partidasVisitante;


    public void inativar() {
        this.ativo = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Sigla getSigla() {
        return sigla;
    }

    public void setSigla(Sigla sigla) {
        this.sigla = sigla;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public Clube setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
        return this;
    }

    public Integer getEmpates() {
        return empates;
    }

    public Clube setEmpates(Integer empates) {
        this.empates = empates;
        return this;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public Clube setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
        return this;
    }

    public Integer getGolsMarcados() {
        return golsMarcados;
    }

    public Clube setGolsMarcados(Integer golsMarcados) {
        this.golsMarcados = golsMarcados;
        return this;
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }

    public Clube setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
        return this;
    }

    public Integer getPontos() {
        return pontos;
    }

    public Clube setPontos(Integer pontos) {
        this.pontos = pontos;
        return this;
    }

    public List<Partida> getPartidasMandante() {
        return partidasMandante;
    }

    public void setPartidasMandante(List<Partida> partidasMandante) {
        this.partidasMandante = partidasMandante;
    }

    public List<Partida> getPartidasVisitante() {
        return partidasVisitante;
    }

    public void setPartidasVisitante(List<Partida> partidasVisitante) {
        this.partidasVisitante = partidasVisitante;
    }
}
