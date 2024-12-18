package com.servicos.cadastro_servicos.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import com.servicos.cadastro_servicos.model.Auditoria;

import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.repository.AuditoriaRepository;
import com.servicos.cadastro_servicos.repository.VisitanteRepository;
import com.servicos.cadastro_servicos.service.VisitanteService;

@Controller
@RequestMapping("/visitantes")
public class VisitanteControlle {

    private final VisitanteService visitanteService; 

    @Autowired
    private VisitanteRepository visitanteRepository; 

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public VisitanteControlle(VisitanteService visitanteService) {
        this.visitanteService = visitanteService;
    }

    
    @GetMapping("/list")
    @ResponseBody
    public List<Visitante> listarVisitante() {
        List<Visitante> visitante = visitanteRepository.findAll();
        return visitante;  
    }


    @GetMapping("/cadastrar")
    public String exibirFormularioELista(Model model) {
        model.addAttribute("visitante", new Visitante());
        List<Visitante> visitantes = visitanteService.listarVisitantes(); 
        model.addAttribute("visitantes", visitantes);
        return "index"; 
    }

    
    @PostMapping("/cadastrar")
@ResponseBody // Garante que o retorno será em JSON
public ResponseEntity<?> cadastrarVisitante(@ModelAttribute Visitante visitante) {
    try {
        // Validação do nome do visitante
        if (visitante.getNome() == null || visitante.getNome().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("O nome do visitante é obrigatório.");
        }

        // Salva o visitante
        visitanteService.salvarVisitante(visitante);

        // Registro de auditoria
        String usuarioLogado = getUsuarioLogado();
        Auditoria auditoria = new Auditoria();
        auditoria.setCreatedBy(usuarioLogado);
        auditoria.setCreatedAt(LocalDateTime.now());
        auditoria.setDescricao("Cadastro de novo visitante: ID " + visitante.getId());
        auditoriaRepository.save(auditoria);

        // Retorna o visitante salvo como JSON
        return ResponseEntity.ok(visitante);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erro ao cadastrar visitante: " + e.getMessage());
    }
}

    


    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        if (visitanteRepository.existsById(id)) {
            visitanteRepository.deleteById(id);


          

        String usuarioLogado = getUsuarioLogado();

        Auditoria auditoria = new Auditoria();
        auditoria.setCreatedBy(usuarioLogado);
        auditoria.setCreatedAt(LocalDateTime.now());
        auditoria.setDescricao("Excluiu um visitante: ID" + id);
        auditoriaRepository.save(auditoria);


            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Visitante> getVisitanteById(@PathVariable Long id) {
        return visitanteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
@ResponseBody
public ResponseEntity<?> atualizarVisitante(@RequestBody Visitante visitanteAtualizado) {
    try {
        Visitante visitanteExistente = visitanteRepository.findById(visitanteAtualizado.getId())
                .orElseThrow(() -> new RuntimeException("Visitante não encontrado"));

        // Captura o usuário logado
        String usuarioLogado = getUsuarioLogado();

        // Armazena alterações em um StringBuilder para auditoria
        StringBuilder alteracoes = new StringBuilder("Atualização do visitante: ");

        if (!visitanteExistente.getNome().equals(visitanteAtualizado.getNome())) {
            alteracoes.append("Nome alterado de ")
                      .append(visitanteExistente.getNome())
                      .append(" para ")
                      .append(visitanteAtualizado.getNome())
                      .append(". ");
            visitanteExistente.setNome(visitanteAtualizado.getNome());
        }

        if (!visitanteExistente.getDocumento().equals(visitanteAtualizado.getDocumento())) {
            alteracoes.append("Documento alterado de ")
                      .append(visitanteExistente.getDocumento())
                      .append(" para ")
                      .append(visitanteAtualizado.getDocumento())
                      .append(". ");
            visitanteExistente.setDocumento(visitanteAtualizado.getDocumento());
        }

        if (!visitanteExistente.getEmpresa().equals(visitanteAtualizado.getEmpresa())) {
            alteracoes.append("Empresa alterada de ")
                      .append(visitanteExistente.getEmpresa())
                      .append(" para ")
                      .append(visitanteAtualizado.getEmpresa())
                      .append(". ");
            visitanteExistente.setEmpresa(visitanteAtualizado.getEmpresa());
        }

        
        visitanteRepository.save(visitanteExistente);

        
        Auditoria auditoria = new Auditoria();
        auditoria.setCreatedBy(usuarioLogado);
        auditoria.setCreatedAt(LocalDateTime.now());
        auditoria.setDescricao(alteracoes.toString());
        auditoriaRepository.save(auditoria);

        return ResponseEntity.ok(visitanteExistente);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar visitante: " + e.getMessage());
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