package com.techlab.excepciones;

public class StockNegativoException extends RuntimeException {
    public StockNegativoException(String mensaje) {
        super(mensaje);
    }
}
