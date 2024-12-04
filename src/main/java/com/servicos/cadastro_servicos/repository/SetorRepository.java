package com.servicos.cadastro_servicos.repository;

import com.servicos.cadastro_servicos.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {
    
}
