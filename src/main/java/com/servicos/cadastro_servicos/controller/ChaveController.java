package com.servicos.cadastro_servicos.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.ui.Model;

import com.servicos.cadastro_servicos.model.Auditoria;
import com.servicos.cadastro_servicos.model.Chave;
import com.servicos.cadastro_servicos.model.RegisChaves;
import com.servicos.cadastro_servicos.repository.AuditoriaRepository;
import com.servicos.cadastro_servicos.repository.ChaveRepository;
import com.servicos.cadastro_servicos.repository.RegisChavesRepository;




@Controller
@RequestMapping("/chave")
public class ChaveController {

    @Autowired
    private ChaveRepository chaveRepository;

    @Autowired
    private RegisChavesRepository regischaveRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;




    @GetMapping("/chaves")
    @ResponseBody
    public List<Chave> listarChaves() {
        return chaveRepository.findAll();
    }
    @GetMapping("/entradas")
    public String pagina(Model model){
    List<RegisChaves> rchaves = regischaveRepository.findAll();
    model.addAttribute("rchaves", rchaves);

    return"chaves";
    }

    @GetMapping("/finalizados")
    public String paginaSai(Model model){
    List<RegisChaves> rchaves = regischaveRepository.findAll();
    model.addAttribute("rchaves", rchaves);

    return"chavesSai";
    }

    @PostMapping("/cadastrar")
public String Cadastrar(@ModelAttribute Chave chave) {
   
    chaveRepository.save(chave);

 
    String usuarioLogado = getUsuarioLogado();

    
    String descricaoAuditoria = "Nova chave cadastrada: " +
                                "ID " + chave.getId() +
                                ", Código: " + (chave.getCod() != null ? chave.getCod() : "Não informado") +
                                ", Local: " + (chave.getLocal() != null ? chave.getLocal() : "Não especificado");

   
    Auditoria auditoria = new Auditoria();
    auditoria.setCreatedBy(usuarioLogado);
    auditoria.setCreatedAt(LocalDateTime.now());
    auditoria.setDescricao(descricaoAuditoria);
    auditoriaRepository.save(auditoria);

    return "redirect:/chave/entradas";
}



    @PostMapping("/registrar")
    public String registrar(@ModelAttribute RegisChaves regischave) {
        regischaveRepository.save(regischave);

        String usuarioLogado = getUsuarioLogado();

        String descricaoAuditoria = "Novo registro de chave: " +
                                    "ID " + regischave.getId() +
                                    ", Solicitante: " + (regischave.getRequisi() != null ? regischave.getRequisi() : "Não informado") +
                                    ", Chave: " + (regischave.getChave() != null ? regischave.getChave().getCod() : "Não especificada");

        Auditoria auditoria = new Auditoria();
        auditoria.setCreatedBy(usuarioLogado);
        auditoria.setCreatedAt(LocalDateTime.now());
        auditoria.setDescricao(descricaoAuditoria);
        auditoriaRepository.save(auditoria);

        return "redirect:/chave/entradas";
    }


    @PutMapping("/atualizarSaida/{id}")
    public ResponseEntity<String> atualizarSaida(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        String hrSaidaString = requestBody.get("hrSaida");
    
        if (hrSaidaString == null || hrSaidaString.isEmpty()) {
            return ResponseEntity.badRequest().body("Horário de saída não pode ser vazio.");
        }
    
        RegisChaves regisChave = regischaveRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro não encontrado"));
    
        try {
            LocalDateTime hrSaida = LocalDateTime.parse(hrSaidaString);
    
           
            regisChave.setDevol(hrSaida);
            regischaveRepository.save(regisChave);
    
            String usuarioLogado = getUsuarioLogado();
    
            String descricaoAuditoria = "Devolução da chave ID: " +
                                        " " + regisChave.getId() + ", Solocitante: " + regisChave.getRequisi();
    
            Auditoria auditoria = new Auditoria();
            auditoria.setCreatedBy(usuarioLogado);
            auditoria.setCreatedAt(LocalDateTime.now());
            auditoria.setDescricao(descricaoAuditoria);
            auditoriaRepository.save(auditoria);
    
            return ResponseEntity.ok("Registro de devolução atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar horário de saída: " + e.getMessage());
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
