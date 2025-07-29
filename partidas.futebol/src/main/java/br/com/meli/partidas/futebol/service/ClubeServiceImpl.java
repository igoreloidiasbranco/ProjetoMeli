package br.com.meli.partidas.futebol.service;

import br.com.meli.partidas.futebol.dto.response.RankingResponseDTO;
import br.com.meli.partidas.futebol.enums.Sigla;
import br.com.meli.partidas.futebol.dto.response.RetrospectoDoClubeResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.exception.IdNotFoundException;
import br.com.meli.partidas.futebol.exception.NomeAndSiglaExistsException;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import br.com.meli.partidas.futebol.utils.Conversao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

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
    public Clube atualizarClube(Clube clubeEditado) {

        isClubeExiste(clubeEditado.getId());
        Clube clubeBanco = clubeRepository.findById(clubeEditado.getId()).orElseThrow(() -> new IdNotFoundException("Id não encontrado"));
        clubeBanco = Conversao.ClubeEditadoToClubeBanco(clubeEditado, clubeBanco);
        isExisteNomeNestaSigla(clubeBanco);
        isDataCriacaoMenorQueDataPartidas(clubeBanco);
        return clubeRepository.save(clubeBanco);
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
        if (nome != null && !nome.isEmpty()) {
            return clubeRepository.findByNome(nome, paginacao);
        }
        if (sigla != null) {
            return clubeRepository.findBySigla(sigla, paginacao);
        }
        if (ativo != null) {
            return clubeRepository.findByAtivo(ativo, paginacao);
        }

        return clubeRepository.findAll(paginacao);
    }

    @Override
    public void isExisteNomeNestaSigla(Clube clube) {

        Clube existente = clubeRepository.findByNomeAndSigla(clube.getNome(), clube.getSigla());
        if (existente != null && !existente.getId().equals(clube.getId())) {
            throw new NomeAndSiglaExistsException("Já existe um clube com esse nome nesta sigla");
        }
    }

    @Override
    public void isClubeExiste(Long id) {
        boolean isClubeExiste = clubeRepository.existsById(id);
        if (!isClubeExiste) {
            throw new IdNotFoundException("Id não encontrado");
        }
    }

    @Override
    public void isDataCriacaoMenorQueDataPartidas(Clube clube) {

        List<Partida> partidasGeralMandante = new ArrayList<>();

        if (clube.getPartidasMandante() != null && !clube.getPartidasMandante().isEmpty()) {
            partidasGeralMandante.addAll(clube.getPartidasMandante());
        }
        if (clube.getPartidasVisitante() != null && !clube.getPartidasVisitante().isEmpty()) {
            partidasGeralMandante.addAll(clube.getPartidasVisitante());
        }

        for (Partida partida : partidasGeralMandante) {

            if (partida.getDataHoraPartida().toLocalDate().isBefore(clube.getDataCriacao())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A data de criação do clube não pode ser maior que a data das partidas");
            }
        }
    }

    @Override
    public RetrospectoDoClubeResponseDTO buscarRetrospectoClube(Long id) {
        Clube clube = buscarClubePorId(id);
        return Conversao.entityToRetrospectoDTO(clube);
    }

    @Override
    public void calcularEstatisticas(Clube clube) {
        int vitorias = 0, empates = 0, derrotas = 0, golsMarcados = 0, golsSofridos = 0, pontos = 0, totalPartidas = 0;

        for (Partida p : clube.getPartidasMandante()) {
            golsMarcados += p.getGolsMandante();
            golsSofridos += p.getGolsVisitante();
            if (p.getGolsMandante() > p.getGolsVisitante()) {
                vitorias++;
                pontos += 3;
            } else if (p.getGolsMandante().equals(p.getGolsVisitante())) {
                empates++;
                pontos += 1;
            } else {
                derrotas++;
            }
        }
        for (Partida p : clube.getPartidasVisitante()) {
            golsMarcados += p.getGolsVisitante();
            golsSofridos += p.getGolsMandante();
            if (p.getGolsVisitante() > p.getGolsMandante()) {
                vitorias++;
                pontos += 3;
            } else if (p.getGolsVisitante().equals(p.getGolsMandante())) {
                empates++;
                pontos += 1;
            } else {
                derrotas++;
            }
        }

        clube.setVitorias(vitorias)
                .setEmpates(empates)
                .setDerrotas(derrotas)
                .setGolsMarcados(golsMarcados)
                .setGolsSofridos(golsSofridos)
                .setPontos(pontos)
                .setTotalPartidas(vitorias + empates + derrotas);

        clubeRepository.save(clube);
    }

    @Override
    public List<RankingResponseDTO> buscarRanking() {
        List<Clube> rankingClubes = clubeRepository.buscarRanking();

        return Conversao.entityToRankingResponseDTO(rankingClubes);
    }
}