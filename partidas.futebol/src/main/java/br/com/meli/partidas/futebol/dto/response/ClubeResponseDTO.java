package br.com.meli.partidas.futebol.dto.response;

import br.com.meli.partidas.futebol.dto.Sigla;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class ClubeResponseDTO {

    private Long id;

    private String nome;

    private Sigla sigla;

    @JsonProperty("data_criacao")
    LocalDate dataCriacao;

    Boolean ativo;

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
