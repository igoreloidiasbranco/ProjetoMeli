package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface EstadioService {

    Estadio salvarEstadio(Estadio estadio);

    void isNomeEstadioExiste(Estadio estadio);

    Estadio atualizarEstadio(Estadio estadio);

    void isEstadioExiste(Long id);

    Estadio buscarEstadioPorId(Long id);

    Page<Estadio> listarEstadios(String nome, Pageable paginacao);

    List<Partida> listarPartidasDoEstadio(Long id);
}