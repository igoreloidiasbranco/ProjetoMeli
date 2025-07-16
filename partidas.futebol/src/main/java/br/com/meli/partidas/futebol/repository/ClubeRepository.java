package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.dto.Sigla;
import br.com.meli.partidas.futebol.entity.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubeRepository extends JpaRepository<Clube, Long> {

    boolean existsByNomeAndSigla(String nome, Sigla sigla);
    Clube findByNomeAndSigla(String nome, Sigla sigla);
    Page<Clube> findByNome(String nome, Pageable paginacao);
    Page<Clube> findBySigla(Sigla sigla, Pageable paginacao);
    Page<Clube> findByAtivo(Boolean ativo, Pageable paginacao);
}
