package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.entity.Estadio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    boolean existsByNome(String nome);

    Page<Estadio> findByNome(String nome, Pageable paginacao);
}
