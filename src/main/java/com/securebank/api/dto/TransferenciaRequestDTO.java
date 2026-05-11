package com.securebank.api.dto;

import java.math.BigDecimal;

public class TransferenciaRequestDTO {

    private Long contaDestinoId;
    private BigDecimal valor;

    public Long getContaDestinoId() {
        return contaDestinoId;
    }

    public void setContaDestinoId(Long contaDestinoId) {
        this.contaDestinoId = contaDestinoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}