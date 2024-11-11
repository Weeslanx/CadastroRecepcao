package com.servicos.cadastro_servicos.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servicos.cadastro_servicos.model.Categoria;
import com.servicos.cadastro_servicos.model.Visita;
import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.repository.CategoriaRepository;
import com.servicos.cadastro_servicos.repository.VisitaRepository;
import com.servicos.cadastro_servicos.repository.VisitanteRepository;

@Controller
public class VisitaController {

    private final VisitanteRepository visitanteRepository;
    private final CategoriaRepository categoriaRepository;
    private final VisitaRepository visitaRepository;

    // Construtor para injeção de dependências
    public VisitaController(VisitanteRepository visitanteRepository,
                            CategoriaRepository categoriaRepository,
                            VisitaRepository visitaRepository) {
        this.visitanteRepository = visitanteRepository;
        this.categoriaRepository = categoriaRepository;
        this.visitaRepository = visitaRepository;
    }

    // Lista todas as visitas cadastradas
    @GetMapping("/registros/visitas")
    public String listarVisitas(Model model) {
        List<Visita> visitas = visitaRepository.findAll(); // Busca todas as visitas

        model.addAttribute("visitas", visitas); // Passa a lista de visitas para o template

        return "index"; // Nome do template HTML que exibe as visitas
    }

    // Lista visitas finalizadas com filtro de datas
    @GetMapping("/registros/visitas/finalizados")
    public String listarVisitasFinalizadas(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        List<Visita> visitas;

        // Filtro de visitas entre duas datas
        if (startDate != null && endDate != null) {
            visitas = visitaRepository.findByHorarioSaidaBetween(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        } else {
            visitas = visitaRepository.findByHorarioSaidaIsNotNull(); // Filtra visitas que já saíram
        }

        model.addAttribute("visitas", visitas); // Passa a lista de visitas para o template

        return "finalizados"; // Nome do template HTML que exibe as visitas finalizadas
    }

    // Exibe o formulário para cadastrar uma nova visita
    @GetMapping("/visitas")
    public String mostrarFormulario(Model model) {
        // Carrega todos os visitantes e categorias para preencher os campos no formulário
        List<Visitante> visitantes = visitanteRepository.findAll();
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("visitantes", visitantes); // Passa a lista de visitantes
        model.addAttribute("categorias", categorias); // Passa a lista de categorias
        model.addAttribute("visita", new Visita()); // Adiciona um objeto vazio de Visita

        return "index"; // Nome do template HTML para o formulário de cadastro
    }

    // Endpoint para carregar dados de visitantes e categorias via AJAX (JSON)
    @GetMapping("/visitas/dados")
    @ResponseBody
    public Map<String, List<?>> carregarDadosVisitas() {
        List<Visitante> visitantes = visitanteRepository.findAll();
        List<Categoria> categorias = categoriaRepository.findAll();
        
        Map<String, List<?>> dados = new HashMap<>();
        dados.put("visitantes", visitantes);
        dados.put("categorias", categorias);
        return dados; // Retorna os dados como JSON
    }

  
    @PostMapping("/visitas/cadastrar")
    public String cadastrarVisita(Visita visita, Model model) {
        System.out.println("Responsável recebido: " + visita.getResponsavel()); // Log para verificar o valor
    
        if (visita.getCategoria() == null || visita.getVisitante() == null || visita.getResponsavel() == null) {
            model.addAttribute("error", "Categoria, Visitante e Responsável são obrigatórios.");
            return "index"; // Redireciona para a página do formulário com uma mensagem de erro
        }
    
        visitaRepository.save(visita); // Salva a nova visita no banco
        return "redirect:/registros/visitas"; // Redireciona para a página de visitas
    }
    
    @DeleteMapping("/visitas/deletar/{id}")
    @ResponseBody
    public ResponseEntity<String> deletarVisita(@PathVariable Long id) {
        if (visitaRepository.existsById(id)) {
            visitaRepository.deleteById(id);
            return ResponseEntity.ok("Visita excluída com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visita não encontrada.");
        }
    }


    // Verifica se um crachá já está em uso (visitante sem horário de saída)
    @GetMapping("/visitas/verificar-cracha")
    @ResponseBody
    public Map<String, Boolean> verificarCracha(@RequestParam int cracha) {
        boolean emUso = visitaRepository.findAll().stream()
            .filter(v -> v.getHorarioSaida() == null) // Filtra visitas sem horário de saída
            .anyMatch(v -> v.getCracha() == cracha); // Verifica se o crachá está em uso

        Map<String, Boolean> response = new HashMap<>();
        response.put("emUso", emUso);
        return response; // Retorna o estado do crachá como JSON
    }

    // Atualiza o horário de saída de uma visita
    @PostMapping("/visitas/atualizar-saida")
    @ResponseBody
    public Map<String, String> atualizarHorarioSaida(
            @RequestParam Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime horarioSaida) {

        Map<String, String> response = new HashMap<>();
        Optional<Visita> visitaOptional = visitaRepository.findById(id);

        if (visitaOptional.isPresent()) {
            Visita visita = visitaOptional.get();
            visita.setHorarioSaida(horarioSaida); // Atualiza o horário de saída da visita
            visitaRepository.save(visita); // Salva a atualização no banco
            response.put("status", "success");
            response.put("message", "Horário de saída atualizado com sucesso.");
        } else {
            response.put("status", "error");
            response.put("message", "Visita não encontrada.");
        }

        return response; // Retorna o status da operação como JSON
    }

    // Exibe a página de login (se necessário)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Nome do arquivo HTML do formulário de login
    }
}
