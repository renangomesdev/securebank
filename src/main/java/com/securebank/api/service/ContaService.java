package com.securebank.api.service;

import com.securebank.api.model.Conta;
import com.securebank.api.repository.ContaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

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

        return repository.save(novaConta);
    }

    public Conta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada com o ID: " + id));
    }

    // Importe esta anotação lá em cima: import org.springframework.transaction.annotation.Transactional;

    @Transactional
    public Conta depositar(Long id, BigDecimal valor) {

        Conta conta = buscarPorId(id);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do depósito deve ser maior que zero!");
        }

        BigDecimal novoSaldo = conta.getSaldo().add(valor);
        conta.setSaldo(novoSaldo);

        return repository.save(conta);
    }
}