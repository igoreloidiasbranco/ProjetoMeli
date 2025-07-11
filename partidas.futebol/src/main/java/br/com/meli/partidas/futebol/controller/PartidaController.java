package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.PartidaResponseDTO;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.service.PartidaService;
import br.com.meli.partidas.futebol.utils.Conversao;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partidas")
public class PartidaController {

    private final PartidaService partidaService;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaResponseDTO salvarPartida(@RequestBody @Valid PartidaRequestDTO partidaRequestDTO) {
        Partida partida = partidaService.salvarPartida(partidaRequestDTO);
        return Conversao.entityToDTO(partida);
    }

}
