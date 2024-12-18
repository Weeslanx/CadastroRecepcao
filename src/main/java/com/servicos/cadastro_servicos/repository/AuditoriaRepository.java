package com.servicos.cadastro_servicos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicos.cadastro_servicos.model.Auditoria;


@Repository
public interface  AuditoriaRepository extends JpaRepository<Auditoria, Long>{


    
}
