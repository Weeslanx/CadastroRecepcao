package com.servicos.cadastro_servicos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.repository.VisitanteRepository;
import com.servicos.cadastro_servicos.service.VisitanteService;

@Controller
@RequestMapping("/visitantes")
public class VisitanteControlle {

    private final VisitanteService visitanteService; 

    @Autowired
    private VisitanteRepository visitanteRepository; 


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
    public String cadastrarVisitante(@ModelAttribute Visitante visitante, Model model) {
        
        if (visitante.getNome() == null || visitante.getNome().trim().isEmpty()) {
            model.addAttribute("erro", "O nome do visitante é obrigatório.");
            List<Visitante> visitantes = visitanteService.listarVisitantes(); 
            model.addAttribute("visitantes", visitantes);
            return "redirect:/registros/visitas";
        }

       
        visitanteService.salvarVisitante(visitante);

        model.addAttribute("visitante", new Visitante()); 
        List<Visitante> visitantes = visitanteService.listarVisitantes(); 
        model.addAttribute("visitantes", visitantes); 
        return "redirect:/registros/visitas"; 
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        if (visitanteRepository.existsById(id)) {
            visitanteRepository.deleteById(id);
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

            // Atualizar os dados do visitante
            visitanteExistente.setNome(visitanteAtualizado.getNome());
            visitanteExistente.setDocumento(visitanteAtualizado.getDocumento());
            visitanteExistente.setEmpresa(visitanteAtualizado.getEmpresa());

            // Salvar as alterações
            visitanteRepository.save(visitanteExistente);

            return ResponseEntity.ok(visitanteExistente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar visitante: " + e.getMessage());
        }
    }
    
   
}