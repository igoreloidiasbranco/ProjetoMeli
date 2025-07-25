package br.com.meli.partidas.futebol.dto.response;

public class RetrospectoDoClubeContraAdversarioResponseDTO {

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

    public RetrospectoDoClubeContraAdversarioResponseDTO setNomeClube(String nomeClube) {
        this.nomeClube = nomeClube;
        return this;
    }

    public RetrospectoDoClubeContraAdversarioResponseDTO setNomeAdversario(String nomeAdversario) {
        this.nomeAdversario = nomeAdversario;
        return this;
    }

    public RetrospectoDoClubeContraAdversarioResponseDTO setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
        return this;
    }

    public RetrospectoDoClubeContraAdversarioResponseDTO setEmpates(Integer empates) {
        this.empates = empates;
        return this;
    }

    public RetrospectoDoClubeContraAdversarioResponseDTO setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
        return this;
    }

    public RetrospectoDoClubeContraAdversarioResponseDTO setGolsMarcados(Integer golsMarcados) {
        this.golsMarcados = golsMarcados;
        return this;
    }

    public RetrospectoDoClubeContraAdversarioResponseDTO setGolsSofridos(Integer golsSofridos) {
        this.golsSofridos = golsSofridos;
        return this;
    }
}
