package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.exception.IdNotFoundException;
import br.com.meli.partidas.futebol.exception.NomeExistsException;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EstadioServiceImpl implements EstadioService{

    EstadioRepository estadioRepository;

    public EstadioServiceImpl(EstadioRepository estadioRepository) {
        this.estadioRepository = estadioRepository;
    }

    @Override
    public Estadio salvarEstadio(Estadio estadio) {

        isNomeEstadioExiste(estadio);

        return estadioRepository.save(estadio);
    }

    @Override
    public Estadio atualizarEstadio(Estadio estadio) {

        isEstadioExiste(estadio.getId());
        isNomeEstadioExiste(estadio);

        return estadioRepository.save(estadio);
    }

    @Override
    public Estadio buscarEstadioPorId(Long id) {
        isEstadioExiste(id);
        return estadioRepository.getReferenceById(id);
    }

    @Override
    public Page<Estadio> listarEstadios(String nome, Pageable paginacao) {
        if(nome != null && !nome.isEmpty()){
            return estadioRepository.findByNome(nome, paginacao);
        }
        return estadioRepository.findAll(paginacao);
    }

    @Override
    public void isEstadioExiste(Long id) {

        boolean existeEstadio = estadioRepository.existsById(id);
        if (!existeEstadio) {
            throw new IdNotFoundException("Id não encontrado");
        }

    }

    @Override
    public void isNomeEstadioExiste(Estadio estadio) {
        boolean isNomeEstadioExiste = estadioRepository.existsByNome(estadio.getNome());
        if (isNomeEstadioExiste) {
            throw new NomeExistsException("Já existe um estádio com esse nome");
        }
    }

    @Override
    public List<Partida> listarPartidasDoEstadio(Long id) {
        isEstadioExiste(id);
        return estadioRepository.findPartidasByEstadioId(id);
    }
}
