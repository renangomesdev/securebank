package com.securebank.api.repository;

import com.securebank.api.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    boolean existsByCpf(String cpf);
    Optional<Conta> findByCpf(String cpf);

}