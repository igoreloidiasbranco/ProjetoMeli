package br.com.meli.partidas.futebol.dto.response;

public class RetrospectoDoClubeResponseDTO {
    private String nomeClube;
    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsMarcados;
    private Integer golsSofridos;

    public RetrospectoDoClubeResponseDTO() {
    }

    public String getNomeClube() {
        return nomeClube;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public Integer getEmpates() {
        return empates;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public Integer getGolsMarcados() {
        return golsMarcados;
    }

    public Integer getGolsSofridos() {
        return golsSofridos;
    }

    public RetrospectoDoClubeResponseDTO setNomeClube(String nomeClube) {
        this.nomeClube = nomeClube;
        return this;
    }

    public RetrospectoDoClubeResponseDTO setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
        return this;
    }

    public RetrospectoDoClubeResponseDTO setEmpates(Integer empates) {
        this.empates = empates;
        return this;
    }

    public RetrospectoDoClubeResponseDTO setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
        return this;
    }

    public RetrospectoDoClubeResponseDTO setGolsMarcados(Integer golsMarcados) {
        this.golsMarcados = golsMarcados;
        return this;
    }

    public RetrospectoDoClubeResponseDTO setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
        return this;
    }

}
