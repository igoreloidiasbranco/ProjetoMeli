package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import org.springframework.stereotype.Service;

@Service
public class ClubeServiceImpl implements ClubeService {

    private final ClubeRepository clubeRepository;

    public ClubeServiceImpl(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    @Override
    public Clube salvarClube(Clube clube) {

        return clubeRepository.save(clube);

    }
}
