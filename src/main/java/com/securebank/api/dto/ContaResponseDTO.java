package com.securebank.api.dto;

import java.math.BigDecimal;

public class ContaResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private BigDecimal saldo;

    public ContaResponseDTO(Long id, String nome, String cpf, BigDecimal saldo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.saldo = saldo;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public BigDecimal getSaldo() { return saldo; }
}