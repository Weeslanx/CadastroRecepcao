package com.servicos.cadastro_servicos.controller;


import com.servicos.cadastro_servicos.model.Auditoria;
import com.servicos.cadastro_servicos.model.Correios;
import com.servicos.cadastro_servicos.repository.AuditoriaRepository;
import com.servicos.cadastro_servicos.repository.CorreiosRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;


@Controller
@RequestMapping("/correios")
public class CorreiosController {

    @Autowired
    private CorreiosRepository correiosRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    
    @GetMapping
    public String exibirPaginaCorreios(Model model) {
   
    List<Correios> listaCorreios = correiosRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    model.addAttribute("Correios", listaCorreios);
    return "correios";
}




    
    @PostMapping("/cadastrar")
public String criarEntrega(Correios novaEntrega) {
  
    correiosRepository.save(novaEntrega);

   
    String usuarioLogado = getUsuarioLogado();

 
    String descricaoAuditoria = "Nova entrega cadastrada: " +
                                "ID " + novaEntrega.getId() +
                                ", Destinatário: " + (novaEntrega.getDestinatario() != null ? novaEntrega.getDestinatario() : "Não informado") +
                                ", Serviço: " + (novaEntrega.getServico() != null ? novaEntrega.getServico() : "Não especificado");

    
    Auditoria auditoria = new Auditoria();
    auditoria.setCreatedBy(usuarioLogado);
    auditoria.setCreatedAt(LocalDateTime.now());
    auditoria.setDescricao(descricaoAuditoria);
    auditoriaRepository.save(auditoria);

    return "redirect:/correios";
}


    
    @GetMapping("/{id}")
    public ResponseEntity<Correios> buscarPorId(@PathVariable Long id) {
        Optional<Correios> correios = correiosRepository.findById(id);

        if (correios.isPresent()) {
            return ResponseEntity.ok(correios.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<Correios> atualizarEntrega(@PathVariable Long id, @RequestBody Correios entregaAtualizada) {
        Optional<Correios> entregaExistente = correiosRepository.findById(id);

        if (entregaExistente.isPresent()) {
            Correios entrega = entregaExistente.get();
            entrega.setFuncionario(entregaAtualizada.getFuncionario());
            entrega.setSetor(entregaAtualizada.getSetor());
            entrega.setHorario(entregaAtualizada.getHorario());
            entrega.setServico(entregaAtualizada.getServico());
            entrega.setCodigoPostal(entregaAtualizada.getCodigoPostal());
            entrega.setDestinatario(entregaAtualizada.getDestinatario());

            correiosRepository.save(entrega);
            return ResponseEntity.ok(entrega);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEntrega(@PathVariable Long id) {
        Optional<Correios> entregaExistente = correiosRepository.findById(id);

        if (entregaExistente.isPresent()) {
            Correios entrega = entregaExistente.get();

           
            String usuarioLogado = getUsuarioLogado();

           
            String descricaoAuditoria = "Exclusão da entrega: ID " + entrega.getId() +
                                        ", Destinatário: " + (entrega.getDestinatario() != null ? entrega.getDestinatario() : "Não informado") +
                                        ", Serviço: " + (entrega.getServico() != null ? entrega.getServico() : "Não especificado");

           
            Auditoria auditoria = new Auditoria();
            auditoria.setCreatedBy(usuarioLogado);
            auditoria.setCreatedAt(LocalDateTime.now());
            auditoria.setDescricao(descricaoAuditoria);
            auditoriaRepository.save(auditoria);

           
            correiosRepository.deleteById(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private String getUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
    
        if (authentication == null || authentication.getPrincipal() == null) {
            return "Usuário desconhecido"; 
        }
    
        Object principal = authentication.getPrincipal();
    
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal != null ? principal.toString() : "Usuário desconhecido";
        }
    }
}
