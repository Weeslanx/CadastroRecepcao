package com.servicos.cadastro_servicos.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.servicos.cadastro_servicos.model.User;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    
  
    UserDetails findByName(String name);

    
}

   
