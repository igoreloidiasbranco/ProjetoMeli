package br.com.meli.partidas.futebol.dto.request;

import br.com.meli.partidas.futebol.enums.Sigla;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class EstadioRequestDTO {

    @NotBlank
    @Pattern(regexp = "^.{3,}$")
    private String nome;

    @NotNull
    private Sigla sigla;


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
}
