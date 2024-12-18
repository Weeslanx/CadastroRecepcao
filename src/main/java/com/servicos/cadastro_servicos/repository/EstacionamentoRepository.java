package com.servicos.cadastro_servicos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.servicos.cadastro_servicos.model.Estacionamento;

public interface  EstacionamentoRepository extends JpaRepository<Estacionamento, Long> {
    
}
