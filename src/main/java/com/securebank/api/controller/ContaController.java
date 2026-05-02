package com.securebank.api.controller;

import com.securebank.api.dto.ContaResponseDTO;
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
}