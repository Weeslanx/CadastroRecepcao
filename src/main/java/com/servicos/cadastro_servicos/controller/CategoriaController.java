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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servicos.cadastro_servicos.model.Auditoria;
import com.servicos.cadastro_servicos.model.Categoria;
import com.servicos.cadastro_servicos.repository.AuditoriaRepository;
import com.servicos.cadastro_servicos.repository.CategoriaRepository;



@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "index"; 
    }

@PostMapping("/novocadastro")
@ResponseBody // Garante que o retorno será em JSON
public ResponseEntity<?> cadastrarCategoria(@ModelAttribute Categoria categoria) {
    try {
        // Salva a categoria
        categoriaRepository.save(categoria);

        // Registra a auditoria
        String usuarioLogado = getUsuarioLogado();
        String descricaoAuditoria = "Nova categoria cadastrada: " +
                                    "ID " + categoria.getId() +
                                    ", Nome: " + (categoria.getNome() != null ? categoria.getNome() : "Não informado");

        Auditoria auditoria = new Auditoria();
        auditoria.setCreatedBy(usuarioLogado);
        auditoria.setCreatedAt(LocalDateTime.now());
        auditoria.setDescricao(descricaoAuditoria);
        auditoriaRepository.save(auditoria);

        // Retorna a categoria salva como JSON
        return ResponseEntity.ok(categoria);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar a categoria: " + e.getMessage());
    }
}




    @GetMapping("/list")
    @ResponseBody
    public List<Categoria> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias;  
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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
