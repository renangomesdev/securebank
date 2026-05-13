package com.securebank.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

    public class DepositoRequestDTO {

        @NotNull(message = "O valor do depósito é obrigatório.")
        @Positive(message = "O valor do depósito deve ser maior que zero.")
        private BigDecimal valor;

        public BigDecimal getValor() {
            return valor;
        }

        public void setValor(BigDecimal valor) {
            this.valor = valor;
        }
    }

