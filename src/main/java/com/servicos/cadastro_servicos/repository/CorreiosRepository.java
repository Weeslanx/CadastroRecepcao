package com.servicos.cadastro_servicos.repository;

import com.servicos.cadastro_servicos.model.Correios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CorreiosRepository extends JpaRepository<Correios, Long> {

    @Query("SELECT c FROM Correios c JOIN FETCH c.setor WHERE c.id = :id")
    Correios findByIdWithSetor(@Param("id") Long id);

    @Query("SELECT c FROM Correios c JOIN FETCH c.setor")
    List<Correios> findAllWithSetor();
}

