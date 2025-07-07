package br.com.meli.partidas.futebol.exception;

public class NomeAndSiglaExistsException extends RuntimeException {

    public NomeAndSiglaExistsException(String msg) {
        super(msg);
    }
}
