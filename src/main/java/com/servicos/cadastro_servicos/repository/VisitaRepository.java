package com.servicos.cadastro_servicos.repository;



import com.servicos.cadastro_servicos.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaRepository extends JpaRepository<Visita, Long> {
}
