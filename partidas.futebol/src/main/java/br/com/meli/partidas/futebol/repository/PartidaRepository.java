package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

    @Query(
            value = "SELECT p.* FROM partidas p " +
                    "JOIN clubes c ON p.id_clube_mandante = c.id " +
                    "OR p.id_clube_visitante = c.id " +
                    "WHERE c.nome = :nomeClube",
            nativeQuery = true)
    Page<Partida> listarPartidasPorClube(String nomeClube, Pageable paginacao);


    @Query(
            value = "SELECT p.* FROM partidas p " +
                    "JOIN estadios e ON p.id_estadio = e.id " +
                    "WHERE e.nome = :nomeEstadio",
            nativeQuery = true)
    Page<Partida> listarPartidasPorEstadio(String nomeEstadio, Pageable paginacao);


    List<Partida> findByIdClubeMandanteAndIdClubeVisitante(Clube clubeUm, Clube clubeDois);

    @Query(
            value = "SELECT * FROM partidas " +
                    "WHERE ABS(gols_mandante - gols_visitante) >= :goleadasComDiferencaGols",
            nativeQuery = true)
    Page<Partida> listarPartidasPorGoleadas(Integer goleadasComDiferencaGols, Pageable paginacao);
}
