package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.repository.ClubeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    final ClubeRepository clubeRepository;

    public ClubeController(ClubeRepository clubeRepository) {
        this.clubeRepository = clubeRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Clube salvarClube(@RequestBody Clube clube) {
        return clubeRepository.save(clube);
    }
}
