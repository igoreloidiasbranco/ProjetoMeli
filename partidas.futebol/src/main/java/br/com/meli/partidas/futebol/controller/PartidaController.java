package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.PartidaRequestDTO;
import br.com.meli.partidas.futebol.dto.response.PartidaResponseDTO;
import br.com.meli.partidas.futebol.entity.Partida;
import br.com.meli.partidas.futebol.service.PartidaService;
import br.com.meli.partidas.futebol.utils.Conversao;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
        Partida partida = partidaService.validarPartida(partidaRequestDTO);

        return Conversao.entityToDTO(partidaService.salvarPartida(partida));
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartidaResponseDTO atualizarPartida(@PathVariable Long id, @RequestBody @Valid PartidaRequestDTO partidaRequestDTO) {
        Partida partida = partidaService.validarPartida(partidaRequestDTO);
        partida.setId(id);
        Partida partidaEditada = partidaService.atualizarPartida(partida);
        return Conversao.entityToDTO(partidaEditada);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarPartida(@PathVariable Long id) {
        partidaService.deletarPartida(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PartidaResponseDTO buscarPartidaPorId(@PathVariable Long id) {
        Partida partidaEncontrada = partidaService.buscarPartidaPorId(id);
        return Conversao.entityToDTO(partidaEncontrada);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PartidaResponseDTO> listarPartidas(
            @RequestParam(required = false) String nomeClube,
            @RequestParam(required = false) String nomeEstadio,
            @PageableDefault(size = 5, sort = {"id"})
            Pageable paginacao) {

        Page<Partida> partidas = partidaService.listarPartidas(nomeClube, nomeEstadio, paginacao);
        return partidas.map(Conversao::entityToDTO);
    }
}
