package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.response.RankingResponseDTO;
import br.com.meli.partidas.futebol.enums.Sigla;
import br.com.meli.partidas.futebol.dto.response.RetrospectoDoClubeResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface ClubeService {

    Clube salvarClube(Clube clube);

    Clube atualizarClube(Clube clube);

    void inativarClube(Long id);

    Clube buscarClubePorId(Long id);

    void isClubeExiste(Long id);

    void isExisteNomeNestaSigla(Clube clube);

    Page<Clube> listarClubes(String nome, Sigla sigla, Boolean ativo, Pageable paginacao);

    void isDataCriacaoMenorQueDataPartidas(Clube clube);

    RetrospectoDoClubeResponseDTO buscarRetrospectoClube(Long id);

    void calcularEstatisticas(Clube clube);

    List<RankingResponseDTO> buscarRanking();
}
