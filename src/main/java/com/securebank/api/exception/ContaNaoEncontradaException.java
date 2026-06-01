package com.securebank.api.exception;
public class ContaNaoEncontradaException extends RuntimeException {
    public ContaNaoEncontradaException(Long id) {
        super("Conta não encontrada com o ID: " + id);
    }
}