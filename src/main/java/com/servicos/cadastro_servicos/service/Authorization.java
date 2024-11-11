package com.servicos.cadastro_servicos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.servicos.cadastro_servicos.repository.UserRepository;

@Service
public class Authorization implements UserDetailsService{

    @Autowired
    UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        return repository.findByName(username);
        
    }

}
    

