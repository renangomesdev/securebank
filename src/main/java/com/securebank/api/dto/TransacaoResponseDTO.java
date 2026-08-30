package com.securebank.api.dto;

import com.securebank.api.model.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(Long id, TipoTransacao tipo, BigDecimal valor, LocalDateTime dataHora) {
}