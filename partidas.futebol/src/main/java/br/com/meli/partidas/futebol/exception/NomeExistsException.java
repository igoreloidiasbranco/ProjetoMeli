package br.com.meli.partidas.futebol.exception;

public class NomeExistsException extends RuntimeException {
    public NomeExistsException(String msg) {
        super(msg);
    }
}
