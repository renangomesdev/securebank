package com.securebank.api.dto;

import java.math.BigDecimal;

public class SaqueRequestDTO {

    private BigDecimal valor;

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}