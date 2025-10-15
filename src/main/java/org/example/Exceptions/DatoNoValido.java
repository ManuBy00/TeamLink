package org.example.Exceptions;

public class DatoNoValido extends RuntimeException {
    public DatoNoValido(String message) {
        super(message);
    }
}
