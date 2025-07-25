package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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


}
