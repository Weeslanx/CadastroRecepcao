package com.servicos.cadastro_servicos.repository;


import com.servicos.cadastro_servicos.model.Chave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChaveRepository extends JpaRepository<Chave, Long> {

}