package com.servicos.cadastro_servicos.controller;

import com.servicos.cadastro_servicos.model.Categoria;
import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.model.Visita;
import com.servicos.cadastro_servicos.repository.CategoriaRepository;
import com.servicos.cadastro_servicos.repository.VisitanteRepository;
import com.servicos.cadastro_servicos.repository.VisitaRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    
    @GetMapping("/registros/visitas/finalizados")
    public String listarVisitasfinalizadas(Model model) {
        List<Visita> visitas = visitaRepository.findAll(); // Busca todas as visitas cadastradas

        model.addAttribute("visitas", visitas); // Passa a lista de visitas para o template

        return "finalizados"; // Nome do template HTML que exibe as visitas
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
    
    @GetMapping("/visitas/verificar-cracha")
    @ResponseBody
    public Map<String, Boolean> verificarCracha(@RequestParam int cracha) {
        boolean emUso = visitaRepository.findAll().stream()
            .filter(v -> v.getHorarioSaida() == null) // Filtra as visitas sem horário de saída
            .anyMatch(v -> v.getCracha() == cracha); // Verifica se o crachá está em uso

        Map<String, Boolean> response = new HashMap<>();
        response.put("emUso", emUso);
        return response; // Retorna a verificação como JSON
    }
    
    
    @PostMapping("/visitas/atualizar-saida")
    @ResponseBody
    public Map<String, String> atualizarHorarioSaida(
            @RequestParam Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime horarioSaida) {

        Map<String, String> response = new HashMap<>();
        Optional<Visita> visitaOptional = visitaRepository.findById(id);

        if (visitaOptional.isPresent()) {
            Visita visita = visitaOptional.get();
            visita.setHorarioSaida(horarioSaida); // Atualiza o horário de saída
            visitaRepository.save(visita); // Salva a atualização no banco
            response.put("status", "success");
            response.put("message", "Horário de saída atualizado com sucesso.");
        } else {
            response.put("status", "error");
            response.put("message", "Visita não encontrada.");
        }

        return response;
    }
}