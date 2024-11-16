package com.servicos.cadastro_servicos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicos.cadastro_servicos.Security.TokenService;
import com.servicos.cadastro_servicos.dto.AuthenticationDTO;
import com.servicos.cadastro_servicos.dto.LoginResponseDTO;
import com.servicos.cadastro_servicos.dto.ResterDTO;
import com.servicos.cadastro_servicos.model.User;
import com.servicos.cadastro_servicos.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.name(), data.pass());
    Authentication auth = this.authenticationManager.authenticate(usernamePassword);

    
    if (auth.getPrincipal() instanceof User user) {
        var token = tokenService.generateToken(user);

        // Retorna o token com status OK
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    return ResponseEntity.status(401).build(); 
}


    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid ResterDTO data) {
        if (this.repository.findByName(data.name()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = passwordEncoder.encode(data.pass());
        User newUser = new User(data.name(), encryptedPassword, data.role());

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
