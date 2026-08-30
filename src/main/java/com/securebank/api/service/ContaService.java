package com.securebank.api.service;

import com.securebank.api.dto.TransacaoResponseDTO;
import com.securebank.api.exception.ContaNaoEncontradaException;
import com.securebank.api.exception.SaldoInsuficienteException;
import com.securebank.api.model.Conta;
import com.securebank.api.model.TipoTransacao;
import com.securebank.api.model.Transacao;
import com.securebank.api.repository.ContaRepository;
import com.securebank.api.repository.TransacaoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ContaRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TransacaoRepository transacaoRepository;

    public ContaService(ContaRepository repository, PasswordEncoder passwordEncoder, TransacaoRepository transacaoRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.transacaoRepository = transacaoRepository;
    }

    public Conta abrirConta(Conta novaConta) {

        if (repository.existsByCpf(novaConta.getCpf())) {
            throw new RuntimeException("Já existe uma conta cadastrada com este CPF!");
        }

        String senhaCriptografada = passwordEncoder.encode(novaConta.getSenha());
        novaConta.setSenha(senhaCriptografada);

        return repository.save(novaConta);
    }

    public Conta buscarPorIdComLock(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }

    public Conta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }

    @Transactional
    public Conta depositar(Long id, BigDecimal valor) {

        Conta conta = buscarPorIdComLock(id);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do depósito deve ser maior que zero!");
        }

        BigDecimal novoSaldo = conta.getSaldo().add(valor);
        conta.setSaldo(novoSaldo);

        registrarTransacao(conta, TipoTransacao.DEPOSITO, valor);
        return repository.save(conta);
    }

    @Transactional
    public Conta sacar(Long id, BigDecimal valor) {

        Conta conta = buscarPorIdComLock(id);

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor do saque deve ser maior que zero!");
        }

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException();
        }

        BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
        conta.setSaldo(novoSaldo);

        registrarTransacao(conta, TipoTransacao.SAQUE, valor);
        return repository.save(conta);
    }

    @Transactional
    public Conta transferir(Long idOrigem, Long idDestino, BigDecimal valor) {

        if (idOrigem.equals(idDestino)) {
            throw new RuntimeException("A conta de origem e destino não podem ser a mesma!");
        }

        Conta contaOrigem;
        Conta contaDestino;

        if (idOrigem < idDestino) {
            contaOrigem = buscarPorIdComLock(idOrigem);
            contaDestino = buscarPorIdComLock(idDestino);
        } else {
            contaDestino = buscarPorIdComLock(idDestino);
            contaOrigem = buscarPorIdComLock(idOrigem);
        }

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("O valor da transferência deve ser maior que zero!");
        }
        if (contaOrigem.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException();
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(valor));
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));

        registrarTransacao(contaOrigem, TipoTransacao.TRANSFERENCIA, valor);
        registrarTransacao(contaDestino, TipoTransacao.TRANSFERENCIA, valor);

        repository.save(contaDestino);
        return repository.save(contaOrigem);
    }

    private void registrarTransacao(Conta conta, TipoTransacao tipo, BigDecimal valor) {
        Transacao transacao = new Transacao();
        transacao.setConta(conta);
        transacao.setTipo(tipo);
        transacao.setValor(valor);
        transacao.setDataHora(LocalDateTime.now());
        transacaoRepository.save(transacao);
    }

    public List<TransacaoResponseDTO> consultarExtrato(Long id) {
        buscarPorId(id); 
        
        List<Transacao> transacoes = transacaoRepository.findByContaIdOrderByDataHoraDesc(id);
        
        return transacoes.stream()
                .map(t -> new TransacaoResponseDTO(t.getId(), t.getTipo(), t.getValor(), t.getDataHora()))
                .collect(Collectors.toList());
    }
}