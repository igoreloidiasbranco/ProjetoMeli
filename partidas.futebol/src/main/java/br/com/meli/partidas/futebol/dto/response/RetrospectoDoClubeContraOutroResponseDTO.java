package br.com.meli.partidas.futebol.dto.response;

public class RetrospectoDoClubeContraOutroResponseDTO {

    private String nomeClube;
    private String nomeAdversario;
    private Integer vitorias;
    private Integer empates;
    private Integer derrotas;
    private Integer golsMarcados;
    private Integer golsSofridos;

    public String getNomeClube() {
        return nomeClube;
    }

    public String getNomeAdversario() {
        return nomeAdversario;
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

    public RetrospectoDoClubeContraOutroResponseDTO setNomeClube(String nomeClube) {
        this.nomeClube = nomeClube;
        return this;
    }

    public RetrospectoDoClubeContraOutroResponseDTO setNomeAdversario(String nomeAdversario) {
        this.nomeAdversario = nomeAdversario;
        return this;
    }

    public RetrospectoDoClubeContraOutroResponseDTO setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
        return this;
    }

    public RetrospectoDoClubeContraOutroResponseDTO setEmpates(Integer empates) {
        this.empates = empates;
        return this;
    }

    public RetrospectoDoClubeContraOutroResponseDTO setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
        return this;
    }

    public RetrospectoDoClubeContraOutroResponseDTO setGolsMarcados(Integer golsMarcados) {
        this.golsMarcados = golsMarcados;
        return this;
    }

    public RetrospectoDoClubeContraOutroResponseDTO setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
        return this;
    }
}
