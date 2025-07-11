package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Long> {

}
