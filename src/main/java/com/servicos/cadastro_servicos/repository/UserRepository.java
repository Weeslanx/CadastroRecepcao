package com.servicos.cadastro_servicos.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.servicos.cadastro_servicos.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    
  
    UserDetails findByName(String name);

    
}

   
