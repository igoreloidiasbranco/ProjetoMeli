package br.com.meli.partidas.futebol.repository;

import br.com.meli.partidas.futebol.entity.Clube;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubeRepository extends JpaRepository<Clube, Long> {


}
