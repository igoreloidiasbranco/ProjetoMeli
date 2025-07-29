package br.com.meli.partidas.futebol.dto.response;

public class RankingResponseDTO {

    private String nome;
    private Integer pontos;
    private Integer golsMarcados;
    private Integer vitorias;
    private Integer totalPartidas;

    public String getNome() {
        return nome;
    }

    public Integer getPontos() {
        return pontos;
    }

    public Integer getGolsMarcados() {
        return golsMarcados;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public Integer getTotalPartidas() {
        return totalPartidas;
    }

    public RankingResponseDTO setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public RankingResponseDTO setPontos(Integer pontos) {
        this.pontos = pontos;
        return this;
    }

    public RankingResponseDTO setGolsMarcados(Integer golsMarcados) {
        this.golsMarcados = golsMarcados;
        return this;
    }

    public RankingResponseDTO setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
        return this;
    }

    public RankingResponseDTO setTotalPartidas(Integer totalPartidas) {
        this.totalPartidas = totalPartidas;
        return this;
    }
}
