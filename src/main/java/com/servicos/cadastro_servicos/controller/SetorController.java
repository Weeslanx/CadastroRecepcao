package com.servicos.cadastro_servicos.controller;

import com.servicos.cadastro_servicos.model.Setor;
import com.servicos.cadastro_servicos.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/setores")
public class SetorController {

    @Autowired
    private SetorRepository setorRepository;

    
    @PostMapping("/cadastrar")
    public String cadastrar(@ModelAttribute Setor setor){
        setorRepository.save(setor);  
        return"";
    }
    @PostMapping
    public ResponseEntity<Setor> registrarSetor(@RequestBody Setor setor) {
        Setor novoSetor = setorRepository.save(setor);
        return ResponseEntity.ok(novoSetor);
    }
    
    @GetMapping
    public ResponseEntity<List<Setor>> listarSetores() {
        List<Setor> setores = setorRepository.findAll();
        return ResponseEntity.ok(setores);
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarSetor(@PathVariable Long id) {
        Optional<Setor> setorExistente = setorRepository.findById(id);
        if (setorExistente.isPresent()) {
            setorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/dropdown")
    public ResponseEntity<List<Setor>> listarSetoresParaDropdown() {
        List<Setor> setores = setorRepository.findAll();
        return ResponseEntity.ok(setores);
    }

}
