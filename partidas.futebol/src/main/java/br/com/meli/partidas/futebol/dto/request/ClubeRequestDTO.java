package br.com.meli.partidas.futebol.dto.request;

import br.com.meli.partidas.futebol.enums.Sigla;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class ClubeRequestDTO {

    @NotBlank
    @Pattern(regexp = "^.{2,}$")
    private String nome;

    @NotNull
    private Sigla sigla;

    @JsonProperty("data_criacao")
    @NotNull
    @PastOrPresent
    private LocalDate dataCriacao;

    @NotNull
    private boolean ativo;

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
}
