package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ClubeResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import br.com.meli.partidas.futebol.service.ClubeService;
import br.com.meli.partidas.futebol.utils.Conversao;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/clubes")
public class ClubeController {

    final ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClubeResponseDTO salvarClube(@RequestBody @Valid ClubeRequestDTO clubeRequestDTO) {

       Clube clube = clubeService.salvarClube(Conversao.dtoToEntity(clubeRequestDTO));
       return Conversao.entityToDTO(clube);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClubeResponseDTO atualizarClube(@PathVariable Long id, @RequestBody @Valid ClubeRequestDTO clubeRequestDTO) {
        Clube clube = Conversao.dtoToEntity(clubeRequestDTO);
        clube.setId(id);
        clube = clubeService.atualizarClube(clube);
        return Conversao.entityToDTO(clube);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarClube(@PathVariable Long id) {
        clubeService.inativarClube(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClubeResponseDTO buscarClubePorId(@PathVariable Long id) {
        Clube clube = clubeService.buscarClubePorId(id);
        return Conversao.entityToDTO(clube);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ClubeResponseDTO> listarClubes(
            @PageableDefault(size = 5, sort = {"nome"}) Pageable paginacao) {

        Page<Clube> listaClubes = clubeService.listarClubes(paginacao);
        Page<ClubeResponseDTO> page = listaClubes.map(clube -> Conversao.entityToDTO(clube));
        return page;
    }

}
