package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.entity.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    boolean existsByNome(String nome);
}
