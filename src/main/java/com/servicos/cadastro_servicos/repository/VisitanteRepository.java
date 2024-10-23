package com.servicos.cadastro_servicos.repository;



import com.servicos.cadastro_servicos.model.Visitante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
}
