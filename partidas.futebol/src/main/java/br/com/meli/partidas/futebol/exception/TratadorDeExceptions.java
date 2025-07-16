package br.com.meli.partidas.futebol.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class TratadorDeExceptions {

    @ExceptionHandler
    public ResponseEntity<String> handleBadRequestException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNomeAndSiglaExistsException(NomeAndSiglaExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNomeExistsException(NomeExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
