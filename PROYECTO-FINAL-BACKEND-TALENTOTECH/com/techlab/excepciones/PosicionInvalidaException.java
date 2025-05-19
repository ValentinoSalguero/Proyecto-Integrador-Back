package com.techlab.excepciones;

public class PosicionInvalidaException extends RuntimeException {
    public PosicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
