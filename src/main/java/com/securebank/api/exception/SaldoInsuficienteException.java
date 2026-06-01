package com.securebank.api.exception;
public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException() {
        super("Saldo insuficiente para realizar esta operação.");
    }
}