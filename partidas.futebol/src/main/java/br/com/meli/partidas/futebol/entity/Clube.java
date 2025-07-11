package br.com.meli.partidas.futebol.entity;

import br.com.meli.partidas.futebol.dto.Sigla;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
