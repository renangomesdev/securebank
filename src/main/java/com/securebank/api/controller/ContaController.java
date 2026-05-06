package com.securebank.api.controller;

import com.securebank.api.dto.ContaResponseDTO;
import com.securebank.api.dto.DepositoRequestDTO;
import com.securebank.api.dto.SaqueRequestDTO;
import com.securebank.api.model.Conta;
import com.securebank.api.service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaResponseDTO> criarConta(@RequestBody Conta novaConta) {

        Conta contaCriada = contaService.abrirConta(novaConta);
        ContaResponseDTO response = new ContaResponseDTO(
                contaCriada.getId(),
                contaCriada.getNome(),
                contaCriada.getCpf(),
                contaCriada.getSaldo()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> consultarConta(@PathVariable Long id) {

        Conta contaEncontrada = contaService.buscarPorId(id);

        ContaResponseDTO dto = new ContaResponseDTO(
                contaEncontrada.getId(),
                contaEncontrada.getNome(),
                contaEncontrada.getCpf(),
                contaEncontrada.getSaldo()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<ContaResponseDTO> depositar(
            @PathVariable Long id,
            @RequestBody DepositoRequestDTO dtoDeEntrada) {

        Conta contaAtualizada = contaService.depositar(id, dtoDeEntrada.getValor());

        ContaResponseDTO resposta = new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNome(),
                contaAtualizada.getCpf(),
                contaAtualizada.getSaldo()
        );

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<ContaResponseDTO> sacar(
            @PathVariable Long id,
            @RequestBody SaqueRequestDTO dtoDeEntrada) {

        // 1. Manda o Service tentar fazer o saque
        Conta contaAtualizada = contaService.sacar(id, dtoDeEntrada.getValor());

        // 2. Prepara a resposta blindada (sem expor a senha)
        ContaResponseDTO resposta = new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNome(),
                contaAtualizada.getCpf(),
                contaAtualizada.getSaldo()
        );

        // 3. Devolve a resposta de sucesso com o novo saldo
        return ResponseEntity.ok(resposta);
    }

}