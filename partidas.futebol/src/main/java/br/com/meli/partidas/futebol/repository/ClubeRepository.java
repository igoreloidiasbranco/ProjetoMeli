package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.enums.Sigla;
import br.com.meli.partidas.futebol.entity.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ClubeRepository extends JpaRepository<Clube, Long> {

    Clube findByNomeAndSigla(String nome, Sigla sigla);
    Page<Clube> findByNome(String nome, Pageable paginacao);
    Page<Clube> findBySigla(Sigla sigla, Pageable paginacao);
    Page<Clube> findByAtivo(Boolean ativo, Pageable paginacao);

    @Query(value = "SELECT * \n" +
            "FROM clubes\n" +
            "WHERE total_partidas != 0 \n" +
            "ORDER BY pontos DESC, gols_marcados DESC, vitorias DESC, total_partidas DESC"
            , nativeQuery = true)
    List<Clube> buscarRanking();
}
