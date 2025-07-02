package br.com.meli.partidas.futebol.exception;

import br.com.meli.partidas.futebol.exception.clube_exception.NomeAndSiglaExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TratadorDeExceptions {

    @ExceptionHandler
    public ResponseEntity<String> handleNomeAndSiglaExistsException(NomeAndSiglaExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
