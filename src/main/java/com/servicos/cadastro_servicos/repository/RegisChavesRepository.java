package com.servicos.cadastro_servicos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.servicos.cadastro_servicos.model.RegisChaves;



public interface RegisChavesRepository extends JpaRepository<RegisChaves, Long>{
    
}