package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.exception.IdNotFoundException;
import br.com.meli.partidas.futebol.exception.clube_exception.NomeAndSiglaExistsException;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClubeServiceImpl implements ClubeService {

    private final ClubeRepository clubeRepository;

    public ClubeServiceImpl(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    @Override
    @Transactional
    public Clube salvarClube(Clube clube) {

        isExisteNomeNestaSigla(clube);

        return clubeRepository.save(clube);

    }

    @Override
    @Transactional
    public Clube atualizarClube(Clube clube) {

        isClubeExiste(clube.getId());
        isExisteNomeNestaSigla(clube);
        return clubeRepository.save(clube);
    }

    @Override
    @Transactional
    public void inativarClube(Long id) {

        isClubeExiste(id);
        Clube clube = clubeRepository.getReferenceById(id);
        clube.inativar();
    }


    public void isExisteNomeNestaSigla(Clube clube) {

        boolean isExisteNomeNestaSigla = clubeRepository.existsByNomeAndSigla(clube.getNome(), clube.getSigla());

        if(isExisteNomeNestaSigla){
            throw new NomeAndSiglaExistsException("Já existe um clube com esse nome nesta sigla");
        }
    }

    public void isClubeExiste(Long id) {
        boolean isClubeExiste = clubeRepository.existsById(id);
        if(! isClubeExiste){
            throw new IdNotFoundException("Id não encontrado");
        }
    }

}
