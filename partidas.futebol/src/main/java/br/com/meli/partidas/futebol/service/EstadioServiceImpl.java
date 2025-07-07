package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.exception.NomeExistsException;
import br.com.meli.partidas.futebol.repository.EstadioRepository;
import org.springframework.stereotype.Service;

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
    public void isNomeEstadioExiste(Estadio estadio) {
        boolean isNomeEstadioExiste = estadioRepository.existsByNome(estadio.getNome());
        if (isNomeEstadioExiste) {
            throw new NomeExistsException("Já existe um estádio com esse nome");
        }
    }
}
