package com.servicos.cadastro_servicos.repository;



import com.servicos.cadastro_servicos.model.Visita;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaRepository extends JpaRepository<Visita, Long> {
	 List<Visita> findByHorarioSaidaBetween(LocalDateTime startDate, LocalDateTime endDate);
	  List<Visita> findByHorarioSaidaIsNotNull();
}
