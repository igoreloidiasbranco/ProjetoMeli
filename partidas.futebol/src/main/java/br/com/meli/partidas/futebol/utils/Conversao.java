package br.com.meli.partidas.futebol.utils;

import br.com.meli.partidas.futebol.dto.request.ClubeRequestDTO;
import br.com.meli.partidas.futebol.dto.response.ClubeResponseDTO;
import br.com.meli.partidas.futebol.entity.Clube;
import org.springframework.beans.BeanUtils;

public class Conversao {

    public static Clube dtoToEntity(ClubeRequestDTO clubeRequestDTO) {
        Clube clube = new Clube();
        BeanUtils.copyProperties(clubeRequestDTO, clube);
        return clube;
    }

    public static ClubeResponseDTO entityToDTO(Clube clube) {
        ClubeResponseDTO clubeResponseDTO = new ClubeResponseDTO();
        BeanUtils.copyProperties(clube, clubeResponseDTO);
        return clubeResponseDTO;
    }


}
