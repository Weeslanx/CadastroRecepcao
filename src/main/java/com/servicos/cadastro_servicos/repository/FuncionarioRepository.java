package com.servicos.cadastro_servicos.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.servicos.cadastro_servicos.model.Funcionarios;

public interface FuncionarioRepository extends JpaRepository<Funcionarios, Long> {
    
    @Query("SELECT f.id AS id, f.name AS name, s.nome AS setorNome " +
       "FROM Funcionarios f JOIN f.setor s")
    List<Map<String, Object>> findAllFuncionariosComSetor();

    boolean existsByName(String name);

}
