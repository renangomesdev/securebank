package com.securebank.api.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferenciaRequestDTO {

    @NotNull(message = "O ID da conta de destino é obrigatório.")
    private Long contaDestinoId;
    @NotNull(message = "O valor da transferência é obrigatório.")
    @Positive(message = "O valor da transferência deve ser maior que zero.")
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