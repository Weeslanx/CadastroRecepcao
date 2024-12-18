package com.servicos.cadastro_servicos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.servicos.cadastro_servicos.model.Categoria;
import com.servicos.cadastro_servicos.repository.CategoriaRepository;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/cadastrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "index"; 
    }

    @PostMapping("/novocadastro")
    public String cadastrarCategoria(@ModelAttribute Categoria categoria) {
        System.out.println("Categoria a ser salva: " + categoria.getNome());
        categoriaRepository.save(categoria);
        System.out.println("Categoria salva com sucesso.");
        return "redirect:/users/cadastroUsers";  
    }

     @GetMapping("/list")
    @ResponseBody
    public List<Categoria> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias;  
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
