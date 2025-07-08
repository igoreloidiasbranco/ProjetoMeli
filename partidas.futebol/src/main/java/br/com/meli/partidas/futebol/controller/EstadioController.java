package br.com.meli.partidas.futebol.controller;

import br.com.meli.partidas.futebol.dto.request.EstadioRequestDTO;
import br.com.meli.partidas.futebol.dto.response.EstadioResponseDTO;
import br.com.meli.partidas.futebol.entity.Estadio;
import br.com.meli.partidas.futebol.service.EstadioService;
import br.com.meli.partidas.futebol.utils.Conversao;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadios")
public class EstadioController {

    private final EstadioService estadioService;


    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadioResponseDTO salvarEstadio(@RequestBody @Valid EstadioRequestDTO estadioRequestDTO) {
        Estadio estadio = estadioService.salvarEstadio(Conversao.dtoToEntity(estadioRequestDTO));
        return Conversao.entityToDTO(estadio);

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstadioResponseDTO atualizarEstadio(@PathVariable Long id, @RequestBody @Valid EstadioRequestDTO estadioRequestDTO) {
        Estadio estadio = Conversao.dtoToEntity(estadioRequestDTO);
        estadio.setId(id);
        estadio = estadioService.atualizarEstadio(estadio);
        return Conversao.entityToDTO(estadio);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EstadioResponseDTO buscarEstadioPorId(@PathVariable Long id) {
        Estadio estadio = estadioService.buscarEstadioPorId(id);
        return Conversao.entityToDTO(estadio);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<EstadioResponseDTO> listarEstadios(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 5, sort = {"nome"}) Pageable paginacao
    ) {
        return estadioService.listarEstadios(nome, paginacao)
                .map(Conversao::entityToDTO);
    }

}
