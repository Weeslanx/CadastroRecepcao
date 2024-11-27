package com.servicos.cadastro_servicos.controller;


import com.servicos.cadastro_servicos.model.Correios;
import com.servicos.cadastro_servicos.repository.CorreiosRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;


@Controller
@RequestMapping("/correios")
public class CorreiosController {

    @Autowired
    private CorreiosRepository correiosRepository;

    // Renderizar a página correios.html
    @GetMapping
    public String exibirPaginaCorreios(Model model) {
    // Busca os dados ordenados em ordem decrescente pelo ID
    List<Correios> listaCorreios = correiosRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    model.addAttribute("Correios", listaCorreios);
    return "correios";
}




    // Cadastrar uma nova entrega (via formulário)
    @PostMapping("/cadastrar")
    public String criarEntrega(Correios novaEntrega) {
        correiosRepository.save(novaEntrega);
        return "redirect:/correios"; // Redireciona para a listagem após o cadastro
    }

    // Buscar uma entrega específica por ID (JSON para uso API)
    @GetMapping("/{id}")
    public ResponseEntity<Correios> buscarPorId(@PathVariable Long id) {
        Optional<Correios> correios = correiosRepository.findById(id);

        if (correios.isPresent()) {
            return ResponseEntity.ok(correios.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Atualizar uma entrega existente
    @PutMapping("/{id}")
    public ResponseEntity<Correios> atualizarEntrega(@PathVariable Long id, @RequestBody Correios entregaAtualizada) {
        Optional<Correios> entregaExistente = correiosRepository.findById(id);

        if (entregaExistente.isPresent()) {
            Correios entrega = entregaExistente.get();
            entrega.setFuncionario(entregaAtualizada.getFuncionario());
            entrega.setSetor(entregaAtualizada.getSetor());
            entrega.setHorario(entregaAtualizada.getHorario());
            entrega.setServico(entregaAtualizada.getServico());
            entrega.setCodigoPostal(entregaAtualizada.getCodigoPostal());
            entrega.setDestinatario(entregaAtualizada.getDestinatario());

            correiosRepository.save(entrega);
            return ResponseEntity.ok(entrega);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Excluir uma entrega
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEntrega(@PathVariable Long id) {
        if (correiosRepository.existsById(id)) {
            correiosRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
