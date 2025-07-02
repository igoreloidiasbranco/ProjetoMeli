package br.com.meli.partidas.futebol.exception;

public class IdNotFoundException extends RuntimeException {

    public IdNotFoundException(String msg) {
        super(msg);
    }
}
