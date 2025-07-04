package br.com.meli.partidas.futebol.entity;

import br.com.meli.partidas.futebol.dto.Sigla;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "clubes")
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Sigla sigla;


    LocalDate dataCriacao;

    Boolean ativo;


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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
