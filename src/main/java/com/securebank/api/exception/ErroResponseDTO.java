package com.securebank.api.exception;

public class ErroResponseDTO {
    private String mensagem;

    public ErroResponseDTO(String mensagem){
        this.mensagem=mensagem;
    }

    public String getMensagem(){
        return mensagem;
    }
}
