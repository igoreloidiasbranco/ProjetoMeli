package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    boolean existsByNome(String nome);

    Page<Estadio> findByNome(String nome, Pageable paginacao);

    @Query(value = "SELECT p FROM Partida p JOIN FETCH p.idEstadio e WHERE e.id = :id")
    List<Partida> findPartidasByEstadioId(Long id);
}
