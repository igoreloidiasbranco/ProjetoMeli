package br.com.meli.partidas.futebol.dto.response;

import br.com.meli.partidas.futebol.dto.Sigla;


public class EstadioResponseDTO {


    private Long id;

    private String nome;

    private Sigla sigla;

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
}
