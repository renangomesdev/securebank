package com.securebank.api.controller;

import com.securebank.api.dto.ContaResponseDTO;
import com.securebank.api.dto.CriarContaRequestDTO;
import com.securebank.api.dto.DepositoRequestDTO;
import com.securebank.api.dto.SaqueRequestDTO;
import com.securebank.api.dto.TransferenciaRequestDTO;
import com.securebank.api.model.Conta;
import com.securebank.api.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaResponseDTO> criarConta(@Valid @RequestBody CriarContaRequestDTO dto) {
        Conta novaConta = new Conta();
        novaConta.setNome(dto.getNome());
        novaConta.setCpf(dto.getCpf());
        novaConta.setSenha(dto.getSenha());

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
    public ResponseEntity<ContaResponseDTO> consultarConta(
            @PathVariable Long id,
            @AuthenticationPrincipal Conta contaAutenticada) {

        if (!contaAutenticada.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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
            @Valid @RequestBody DepositoRequestDTO dtoDeEntrada,
            @AuthenticationPrincipal Conta contaAutenticada) {

        if (!contaAutenticada.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

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
            @Valid @RequestBody SaqueRequestDTO dtoDeEntrada,
            @AuthenticationPrincipal Conta contaAutenticada) {

        if (!contaAutenticada.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Conta contaAtualizada = contaService.sacar(id, dtoDeEntrada.getValor());
        ContaResponseDTO resposta = new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNome(),
                contaAtualizada.getCpf(),
                contaAtualizada.getSaldo()
        );

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/{id}/transferencia")
    public ResponseEntity<ContaResponseDTO> transferir(
            @PathVariable Long id,
            @Valid @RequestBody TransferenciaRequestDTO dtoDeEntrada,
            @AuthenticationPrincipal Conta contaAutenticada) {

        if (!contaAutenticada.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Conta contaAtualizada = contaService.transferir(
                id,
                dtoDeEntrada.getContaDestinoId(),
                dtoDeEntrada.getValor()
        );
        ContaResponseDTO resposta = new ContaResponseDTO(
                contaAtualizada.getId(),
                contaAtualizada.getNome(),
                contaAtualizada.getCpf(),
                contaAtualizada.getSaldo()
        );

        return ResponseEntity.ok(resposta);
    }
}