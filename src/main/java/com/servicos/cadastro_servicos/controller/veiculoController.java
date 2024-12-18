package com.servicos.cadastro_servicos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servicos.cadastro_servicos.repository.AuditoriaRepository;
import com.servicos.cadastro_servicos.repository.VeiculoRepository;
import com.servicos.cadastro_servicos.model.Auditoria;
import com.servicos.cadastro_servicos.model.Veiculo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import java.util.Map;



@Controller
@RequestMapping("/veiculos")
public class veiculoController {
   
    @Autowired
    private VeiculoRepository veiculoRepository;

    
    @Autowired
    private AuditoriaRepository auditoriaRepository;


@PostMapping("/cadastrar")
@ResponseBody
public ResponseEntity<Map<String, String>> cadastroVeiculos(@RequestBody Veiculo veiculo) {
    // Salva o veículo no banco de dados
    veiculoRepository.save(veiculo);

    // Após salvar, obtemos o ID gerado
    Long veiculoId = veiculo.getId();
    System.out.println("Veículo salvo com sucesso. ID: " + veiculoId);

    // Auditoria do cadastro
    String usuarioLogado = getUsuarioLogado();
    Auditoria auditoria = new Auditoria();
    auditoria.setCreatedBy(usuarioLogado);
    auditoria.setCreatedAt(LocalDateTime.now());
    auditoria.setDescricao("Cadastro de veículo: ID: " + veiculoId + ", Placa: " + veiculo.getPlaca());
    auditoriaRepository.save(auditoria);

    // Retorna o veículo cadastrado como JSON
    Map<String, String> response = new HashMap<>();
    response.put("id", veiculoId.toString());
    response.put("placa", veiculo.getPlaca());
    response.put("modelo", veiculo.getModelo());

    return ResponseEntity.ok(response);
}




    @GetMapping("/dropdown")
    public ResponseEntity<List<Veiculo>> listarVeiculos(){
        List<Veiculo>  veiculos = veiculoRepository.findAll();


        return ResponseEntity.ok(veiculos);
    }

        private String getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication == null || authentication.getPrincipal() == null) {
            return "Usuário desconhecido"; // Valor padrão para casos nulos
        }
    
        Object principal = authentication.getPrincipal();
    
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal != null ? principal.toString() : "Usuário desconhecido";
        }
    }
    
}
