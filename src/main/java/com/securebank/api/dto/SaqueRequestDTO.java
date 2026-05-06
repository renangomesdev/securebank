package com.securebank.api.dto;

import java.math.BigDecimal;

public class SaqueRequestDTO {

    private BigDecimal valor;

    // Getters e Setters
    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}