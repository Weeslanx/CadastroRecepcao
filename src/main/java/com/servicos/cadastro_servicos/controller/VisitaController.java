package com.servicos.cadastro_servicos.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servicos.cadastro_servicos.model.Auditoria;
import com.servicos.cadastro_servicos.model.Categoria;
import com.servicos.cadastro_servicos.model.Visita;
import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.repository.AuditoriaRepository;
import com.servicos.cadastro_servicos.repository.CategoriaRepository;
import com.servicos.cadastro_servicos.repository.VisitaRepository;
import com.servicos.cadastro_servicos.repository.VisitanteRepository;

@Controller
public class VisitaController {

    private final VisitanteRepository visitanteRepository;
    private final CategoriaRepository categoriaRepository;
    private final VisitaRepository visitaRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

  
    
    public VisitaController(VisitanteRepository visitanteRepository,
                            CategoriaRepository categoriaRepository,
                            VisitaRepository visitaRepository) {
        this.visitanteRepository = visitanteRepository;
        this.categoriaRepository = categoriaRepository;
        this.visitaRepository = visitaRepository;
    }

    
    @GetMapping("/registros/visitas")
    public String listarVisitas(Model model) {
        List<Visita> visitas = visitaRepository.findAll(); 

        model.addAttribute("visitas", visitas); 

        return "index"; 
    }

    
    @GetMapping("/registros/visitas/finalizados")
    public String listarVisitasFinalizadas(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {
    
        List<Visita> visitas;
    
        if (startDate != null && endDate != null) {
            
            visitas = visitaRepository.findByHorarioSaidaBetweenOrderByIdDesc(startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        } else {
            
            visitas = visitaRepository.findByHorarioSaidaIsNotNullOrderByIdDesc();
        }
    
        model.addAttribute("visitas", visitas); 
    
        return "finalizados"; 
    }
    

    
    

   
    @GetMapping("/visitas/dados")
    @ResponseBody
    public Map<String, List<?>> carregarDadosVisitas() {
        List<Visitante> visitantes = visitanteRepository.findAll();
        List<Categoria> categorias = categoriaRepository.findAll();
        
        Map<String, List<?>> dados = new HashMap<>();
        dados.put("visitantes", visitantes);
        dados.put("categorias", categorias);
        return dados;
    }

    @GetMapping("/visitas/contagem")
    @ResponseBody
    public Map<String, Integer> contarTotalDeVisitas() {
        long totalVisitas = visitaRepository.count();
        Map<String, Integer> resultado = new HashMap<>();
        resultado.put("totalVisitas", (int) totalVisitas);
        return resultado;
    }


  
    @PostMapping("/visitas/cadastrar")
public String cadastrarVisita(Visita visita, Model model) {
    System.out.println("Responsável recebido: " + visita.getResponsavel()); 
    
  
    if (visita.getCategoria() == null || visita.getVisitante() == null || visita.getResponsavel() == null) {
        model.addAttribute("error", "Categoria, Visitante e Responsável são obrigatórios.");
        return "index"; 
    }
    
    
    String usuarioLogado = getUsuarioLogado();

    
    visitaRepository.save(visita);

    
    Auditoria auditoria = new Auditoria();
    auditoria.setCreatedBy(usuarioLogado);
    auditoria.setCreatedAt(LocalDateTime.now());
    auditoria.setDescricao("Nova visita cadastrada: ID: " +visita.getId());
    auditoriaRepository.save(auditoria);

   
    

    return "redirect:/registros/visitas"; 
}
    
@DeleteMapping("/visitas/deletar/{id}")
@ResponseBody
public ResponseEntity<String> deletarVisita(@PathVariable Long id) {
    if (visitaRepository.existsById(id)) {
        Visita visita = visitaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visita não encontrada"));

       
        String usuarioLogado = getUsuarioLogado();

      
        String descricaoAuditoria = "Visita excluída: ID " + visita.getId() + ", Nome: " +
                (visita.getVisitante() != null ? visita.getVisitante().getNome() : "Visitante não especificado");

        
        Auditoria auditoria = new Auditoria();
        auditoria.setCreatedBy(usuarioLogado);
        auditoria.setCreatedAt(LocalDateTime.now());
        auditoria.setDescricao(descricaoAuditoria);
        auditoriaRepository.save(auditoria);

     
        visitaRepository.deleteById(id);

        return ResponseEntity.ok("Visita excluída com sucesso.");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Visita não encontrada.");
    }
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

        
            visita.setHorarioSaida(horarioSaida);
            visitaRepository.save(visita);

            
            String usuarioLogado = getUsuarioLogado();

        
            String descricaoAuditoria = "Atualização do horário de saída da visita: " +
                    "ID " + visita.getId() + ", Nome:" + visita.getVisitante() ;

            
            Auditoria auditoria = new Auditoria();
            auditoria.setCreatedBy(usuarioLogado);
            auditoria.setCreatedAt(LocalDateTime.now());
            auditoria.setDescricao(descricaoAuditoria);
            auditoriaRepository.save(auditoria);

            response.put("status", "success");
            response.put("message", "Horário de saída atualizado com sucesso.");
        } else {
            response.put("status", "error");
            response.put("message", "Visita não encontrada.");
        }

        return response;
    }



    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }

    @GetMapping("/porVisitante/{visitanteId}")
    @ResponseBody
    public List<Visita> listarVisitasPorVisitante(@PathVariable Long visitanteId) {
        return visitaRepository.findByVisitanteId(visitanteId);  
    }





     
    @GetMapping("/visitas/verificar-cracha")
    @ResponseBody
    public Map<String, Boolean> verificarCracha(@RequestParam int cracha) {
        boolean emUso = visitaRepository.findAll().stream()
            .filter(v -> v.getHorarioSaida() == null) 
            .anyMatch(v -> v.getCracha() == cracha); 

        Map<String, Boolean> response = new HashMap<>();
        response.put("emUso", emUso);
        return response; 
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
