package br.com.meli.partidas.futebol.entity;

import br.com.meli.partidas.futebol.enums.Sigla;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "estadios")
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(length = 2)
    @Enumerated(EnumType.STRING)
    private Sigla sigla;

    @OneToMany(mappedBy = "idEstadio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partida> partidas;


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

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void setPartidas(List<Partida> partidas) {
        this.partidas = partidas;
    }
}
