package com.servicos.cadastro_servicos.controller;

import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.VisitanteService; // Importando o serviço


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class VisitanteControlle {

    private final VisitanteService visitanteService; // Injetando o serviço


    public VisitanteControlle(VisitanteService visitanteService) {
        this.visitanteService = visitanteService;
    }

    // Exibe o formulário de cadastro e a lista de visitantes na mesma página
    @GetMapping("/visitantes/cadastrar")
    public String exibirFormularioELista(Model model) {
        model.addAttribute("visitante", new Visitante());
        List<Visitante> visitantes = visitanteService.listarVisitantes(); // Obtendo a lista do banco de dados
        model.addAttribute("visitantes", visitantes);
        return "index"; // nome do template do HTML
    }

    // Cadastra o visitante e atualiza a lista
    @PostMapping("/visitantes/cadastrar")
    public String cadastrarVisitante(@ModelAttribute Visitante visitante, Model model) {
        // Valida se o nome está vazio
        if (visitante.getNome() == null || visitante.getNome().trim().isEmpty()) {
            model.addAttribute("erro", "O nome do visitante é obrigatório.");
            List<Visitante> visitantes = visitanteService.listarVisitantes(); // Obtendo a lista do banco de dados
            model.addAttribute("visitantes", visitantes);
            return "redirect:/registros/visitas";
        }

        // Salva o visitante no banco de dados
        visitanteService.salvarVisitante(visitante);

        model.addAttribute("visitante", new Visitante()); // Limpa o formulário após o cadastro
        List<Visitante> visitantes = visitanteService.listarVisitantes(); // Obtendo a lista atualizada
        model.addAttribute("visitantes", visitantes); // Atualiza a lista com o novo visitante
        return "index"; // Mantém na mesma página
    }
}