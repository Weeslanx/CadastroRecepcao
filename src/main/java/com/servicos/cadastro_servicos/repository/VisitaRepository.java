package com.servicos.cadastro_servicos.repository;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.servicos.cadastro_servicos.model.Visita;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {
	 List<Visita> findByHorarioSaidaBetween(LocalDateTime startDate, LocalDateTime endDate);
	  List<Visita> findByHorarioSaidaIsNotNull();
	  List<Visita> findByVisitanteId(Long visitanteId);


	  @Query("SELECT MAX(v.horarioEntrada) FROM Visita v")
		LocalDateTime findUltimaModificacao();


	  
}
