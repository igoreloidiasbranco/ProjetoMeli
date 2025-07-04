package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Clube;

public interface ClubeService {

    Clube salvarClube(Clube clube);

    Clube atualizarClube(Clube clube);

    void inativarClube(Long id);
}
