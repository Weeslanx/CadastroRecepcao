package com.servicos.cadastro_servicos.controller;

import java.util.List; 

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.service.VisitanteService;

@Controller
public class VisitanteControlle {

    private final VisitanteService visitanteService; 


    public VisitanteControlle(VisitanteService visitanteService) {
        this.visitanteService = visitanteService;
    }

    
    @GetMapping("/visitantes/cadastrar")
    public String exibirFormularioELista(Model model) {
        model.addAttribute("visitante", new Visitante());
        List<Visitante> visitantes = visitanteService.listarVisitantes(); 
        model.addAttribute("visitantes", visitantes);
        return "index"; 
    }

    
    @PostMapping("/visitantes/cadastrar")
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
}