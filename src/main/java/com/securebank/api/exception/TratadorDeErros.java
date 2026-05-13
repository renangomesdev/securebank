package com.securebank.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroRegraDeNegocio(RuntimeException ex) {
        ErroResponseDTO erro = new ErroResponseDTO(ex.getMessage());
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> tratarErroValidacao(MethodArgumentNotValidException ex) {

        String mensagem = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        ErroResponseDTO erro = new ErroResponseDTO(mensagem);
        return ResponseEntity.badRequest().body(erro);
    }
}