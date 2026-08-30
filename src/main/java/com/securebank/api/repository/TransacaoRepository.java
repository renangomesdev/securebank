package com.securebank.api.repository;

import com.securebank.api.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaIdOrderByDataHoraDesc(Long contaId);
}