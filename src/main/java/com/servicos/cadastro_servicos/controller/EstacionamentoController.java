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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import com.servicos.cadastro_servicos.model.Auditoria;
import com.servicos.cadastro_servicos.model.Estacionamento;
import com.servicos.cadastro_servicos.model.Veiculo;
import com.servicos.cadastro_servicos.repository.AuditoriaRepository;
import com.servicos.cadastro_servicos.repository.EstacionamentoRepository;
import com.servicos.cadastro_servicos.repository.VeiculoRepository;

import jakarta.servlet.http.HttpServletRequest;




@Controller
@RequestMapping("/estacionamento")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

   
    @GetMapping("/entradas")
    public String paginaEntradas(Model model){
        List<Estacionamento> estacionamentos = estacionamentoRepository.findAll();
        model.addAttribute("estacionamentos", estacionamentos);
        return "estacionamento";
    }

    @GetMapping("/saidas")
    public String paginasaidas(Model model){


        List<Estacionamento> estacionamentos = estacionamentoRepository.findAll();
        model.addAttribute("estacionamentos", estacionamentos);
        return "estacionamentoSai";
    }


    @PostMapping("/cadastrar")
    public RedirectView cadastrar(
            @RequestParam Long veiculoId,
            @RequestParam String motorista,
            @RequestParam String hrEntrada,
            @RequestParam(required = false) String hrSaida,
            HttpServletRequest request // Para capturar a URL anterior (Referer)
    ) {
        try {
            // Busca o veículo pelo ID
            Veiculo veiculo = veiculoRepository.findById(veiculoId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado"));
    
            // Converte os horários para LocalDateTime
            LocalDateTime entrada = LocalDateTime.parse(hrEntrada);
            LocalDateTime saida = (hrSaida != null && !hrSaida.isEmpty()) ? LocalDateTime.parse(hrSaida) : null;
    
            // Registra a auditoria
            String usuarioLogado = getUsuarioLogado();
            Auditoria auditoria = new Auditoria();
            auditoria.setCreatedBy(usuarioLogado);
            auditoria.setCreatedAt(LocalDateTime.now());
            auditoria.setDescricao("Registrada a entrada no estacionamento para o veículo de placa: " + veiculo.getPlaca());
            auditoriaRepository.save(auditoria);
    
            // Salva o registro no estacionamento
            Estacionamento estacionamento = new Estacionamento(veiculo, motorista, entrada, saida, auditoria);
            estacionamentoRepository.save(estacionamento);
    
            // Captura a URL anterior (Referer) ou redireciona para um padrão
            String referer = request.getHeader("Referer");
            return new RedirectView((referer != null) ? referer : "/estacionamento/entradas");
        } catch (ResponseStatusException ex) {
            throw ex; // Lança novamente a exceção específica
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao registrar a entrada: " + e.getMessage());
        }
    }
    






    @DeleteMapping("/{id}")
public ResponseEntity<Void> excluirRegistro(@PathVariable Long id) {
    if (estacionamentoRepository.existsById(id)) {
        Estacionamento estacionamento = estacionamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estacionamento não encontrado"));

        // Captura o usuário logado
        String usuarioLogado = getUsuarioLogado();

        // Cria e salva a auditoria
        Auditoria auditoria = new Auditoria();
        auditoria.setCreatedBy(usuarioLogado);
        auditoria.setCreatedAt(LocalDateTime.now());
        auditoria.setDescricao("Exclusão de registro no estacionamento: " +
        "ID " + estacionamento.getId() + 
        ", Placa: " + estacionamento.getVeiculo().getPlaca() + 
        ", Motorista: " + estacionamento.getMotorista() + 
        ", Hora de Entrada: " + estacionamento.getHrEntrada() + 
        ", Hora de Saída: " + (estacionamento.getHrSaida() != null ? estacionamento.getHrSaida() : "Não registrado"));

        auditoriaRepository.save(auditoria);

        // Remove o registro do estacionamento
        estacionamentoRepository.delete(estacionamento);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

    

      @PutMapping("/atualizarSaida/{id}")
    public ResponseEntity<String> atualizarSaida(
            @PathVariable Long id,
            @RequestBody Map<String, String> requestBody) {
        String hrSaidaString = requestBody.get("hrSaida");

        if (hrSaidaString == null || hrSaidaString.isEmpty()) {
            return ResponseEntity.badRequest().body("Horário de saída não pode ser vazio.");
        }

        Estacionamento estacionamento = estacionamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estacionamento não encontrado"));

        LocalDateTime hrSaida = LocalDateTime.parse(hrSaidaString);
        estacionamento.setHrSaida(hrSaida);
        estacionamentoRepository.save(estacionamento);

        return ResponseEntity.ok("Horário de saída atualizado com sucesso.");
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

    

    

