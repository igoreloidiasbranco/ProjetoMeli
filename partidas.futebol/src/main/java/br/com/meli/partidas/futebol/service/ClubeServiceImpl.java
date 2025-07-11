package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.Sigla;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.exception.IdNotFoundException;
import br.com.meli.partidas.futebol.exception.NomeAndSiglaExistsException;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Clube buscarClubePorId(Long id) {
        isClubeExiste(id);
        return clubeRepository.getReferenceById(id);
    }

    @Override
    public Page<Clube> listarClubes(String nome, Sigla sigla, Boolean ativo, Pageable paginacao) {
        if(nome != null && !nome.isEmpty()){
            return clubeRepository.findByNome(nome, paginacao);
        }
        if(sigla != null) {
            return clubeRepository.findBySigla(sigla, paginacao);
        }
        if(ativo != null) {
            return clubeRepository.findByAtivo(ativo, paginacao);
        }

        return clubeRepository.findAll(paginacao);
    }

    @Override
    public void isExisteNomeNestaSigla(Clube clube) {

        boolean isExisteNomeNestaSigla = clubeRepository.existsByNomeAndSigla(clube.getNome(), clube.getSigla());

        if(isExisteNomeNestaSigla){
            throw new NomeAndSiglaExistsException("Já existe um clube com esse nome nesta sigla");
        }
    }

    @Override
    public void isClubeExiste(Long id) {
        boolean isClubeExiste = clubeRepository.existsById(id);
        if(! isClubeExiste){
            throw new IdNotFoundException("Id não encontrado");
        }
    }

}
