package com.securebank.api.repository;

import com.securebank.api.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByCpf(String cpf);
}