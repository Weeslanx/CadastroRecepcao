package com.servicos.cadastro_servicos.controller;

import com.servicos.cadastro_servicos.model.Categoria;
import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.model.Visita;
import com.servicos.cadastro_servicos.repository.CategoriaRepository;
import com.servicos.cadastro_servicos.repository.VisitanteRepository;
import com.servicos.cadastro_servicos.repository.VisitaRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VisitaController {

    private final VisitanteRepository visitanteRepository;
    private final CategoriaRepository categoriaRepository;
    private final VisitaRepository visitaRepository;

    public VisitaController(VisitanteRepository visitanteRepository,
                            CategoriaRepository categoriaRepository,
                            VisitaRepository visitaRepository) {
        this.visitanteRepository = visitanteRepository;
        this.categoriaRepository = categoriaRepository;
        this.visitaRepository = visitaRepository;
    }
    
    @GetMapping("/registros/visitas")
    public String listarVisitas(Model model) {
        List<Visita> visitas = visitaRepository.findAll(); // Busca todas as visitas cadastradas

        model.addAttribute("visitas", visitas); // Passa a lista de visitas para o template

        return "index"; // Nome do template HTML que exibe as visitas
    }

    @GetMapping("/visitas")
    public String mostrarFormulario(Model model) {
        // Busca todos os visitantes e categorias apenas para o formulário inicial
        List<Visitante> visitantes = visitanteRepository.findAll(); 
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("visitantes", visitantes); // Passa a lista de visitantes para o template
        model.addAttribute("categorias", categorias); // Passa a lista de categorias para o template
        model.addAttribute("visita", new Visita()); // Adiciona um objeto vazio de Visita para o formulário

        return "index"; // O nome do seu template HTML
    }

    @GetMapping("/visitas/dados")
    @ResponseBody
    public Map<String, List<?>> carregarDadosVisitas() {
        List<Visitante> visitantes = visitanteRepository.findAll(); // Busca todos os visitantes
        List<Categoria> categorias = categoriaRepository.findAll(); // Busca todas as categorias
        
        Map<String, List<?>> dados = new HashMap<>();
        dados.put("visitantes", visitantes);
        dados.put("categorias", categorias);
        return dados; // Retorna os dados como JSON
    }

    @PostMapping("/visitas/cadastrar")
    public String cadastrarVisita(Visita visita) {
        visitaRepository.save(visita); // Salva a nova visita no repositório
        return "redirect:/registros/visitas"; // Redireciona para a mesma página para exibir a lista atualizada
    }
}
