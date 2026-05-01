package com.securebank.api.service;

import com.securebank.api.model.Conta;
import com.securebank.api.repository.ContaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ContaService {

    private final ContaRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ContaService(ContaRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Conta abrirConta(Conta novaConta) {

        if (repository.findByCpf(novaConta.getCpf()).isPresent()) {
            throw new RuntimeException("Já existe uma conta cadastrada com este CPF!");
        }

        String senhaCriptografada = passwordEncoder.encode(novaConta.getSenha());
        novaConta.setSenha(senhaCriptografada);

        if (novaConta.getSaldo() == null) {
            novaConta.setSaldo(java.math.BigDecimal.ZERO);
        }

        return repository.save(novaConta);
    }
}