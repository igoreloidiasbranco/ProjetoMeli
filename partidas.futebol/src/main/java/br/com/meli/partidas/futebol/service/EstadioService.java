package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Estadio;

public interface EstadioService {

    Estadio salvarEstadio(Estadio estadio);

    void isNomeEstadioExiste(Estadio estadio);

    Estadio atualizarEstadio(Estadio estadio);

    void isEstadioExiste(Long id);

    Estadio buscarEstadioPorId(Long id);
}