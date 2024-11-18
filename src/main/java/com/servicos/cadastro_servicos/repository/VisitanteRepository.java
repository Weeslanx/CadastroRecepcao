package com.servicos.cadastro_servicos.repository;



import com.servicos.cadastro_servicos.model.Visitante;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
	
	
}
