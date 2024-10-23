package com.servicos.cadastro_servicos;



import com.servicos.cadastro_servicos.model.Visitante;
import com.servicos.cadastro_servicos.repository.VisitanteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitanteService {

    private final VisitanteRepository visitanteRepository;
    
    public VisitanteService(VisitanteRepository visitanteRepository) {
        this.visitanteRepository = visitanteRepository;
    }

    public Visitante salvarVisitante(Visitante visitante) {
        return visitanteRepository.save(visitante);
    }

    // Método para listar todos os visitantes
    public List<Visitante> listarVisitantes() {
        return visitanteRepository.findAll();
    }
}
