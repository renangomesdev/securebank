package com.securebank.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> tratarNaoEncontrado(ContaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErroResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErroResponseDTO> tratarSaldoInsuficiente(SaldoInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErroResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroRegraDeNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ErroResponseDTO(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErroResponseDTO(mensagem));
    }
}