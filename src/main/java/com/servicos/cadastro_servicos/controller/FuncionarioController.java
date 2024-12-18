package com.servicos.cadastro_servicos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collections;

import com.servicos.cadastro_servicos.model.Funcionarios;
import com.servicos.cadastro_servicos.model.Setor;
import com.servicos.cadastro_servicos.repository.FuncionarioRepository;
import com.servicos.cadastro_servicos.repository.SetorRepository;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private SetorRepository setorRepository;



    @GetMapping("/listar")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> listarFuncionarios() {
        try {
            List<Map<String, Object>> funcionarios = funcionarioRepository.findAllFuncionariosComSetor();
            return ResponseEntity.ok(funcionarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }


    @PostMapping("/cadastrar")
    @ResponseBody
    public ResponseEntity<?> cadastrarFuncionario(
            @RequestParam String primeiroNome,
            @RequestParam String ultimoNome,
            @RequestParam Long setorId) {
    
        try {
            // Verifica se o setor existe
            Setor setor = setorRepository.findById(setorId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Setor não encontrado"));
    
            // Formata o nome completo
            String nomeCompleto = formatarNome(primeiroNome) + " " + formatarNome(ultimoNome);
    
            // Verifica se o funcionário já existe
            boolean exists = funcionarioRepository.existsByName(nomeCompleto);
            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Já existe um funcionário com o nome: " + nomeCompleto);
            }
    
            // Cria e salva o novo funcionário
            Funcionarios funcionario = new Funcionarios();
            funcionario.setName(nomeCompleto);
            funcionario.setSetor(setor);
            funcionarioRepository.save(funcionario);
    
            // Retorna os dados do novo funcionário
            Map<String, Object> response = new HashMap<>();
            response.put("id", funcionario.getId());
            response.put("name", funcionario.getName());
            response.put("setorNome", setor.getNome());
    
            return ResponseEntity.ok(response);
    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }
    


// Método auxiliar para formatar o nome
private String formatarNome(String nome) {
    return nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
}

}