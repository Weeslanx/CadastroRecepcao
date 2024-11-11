package com.servicos.cadastro_servicos.Security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.servicos.cadastro_servicos.model.User;

@Service
public class TokenService {
    private final String secret = "aaaaa";

    public String generateToken(User user) {
        try {
            System.out.println("Chave secreta usada na geração: " + secret); // Log para verificação
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getName())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }
    
    public String validateToken(String token) {
        try {
            
            System.out.println("Chave secreta usada na validação: " + secret);
            System.out.println("Token recebido para validação: " + token); // Log para verificação
            System.out.println("Validando token: " + token);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println("Erro ao validar o token: " + exception.getMessage());
            return null; // Retorne null para tokens inválidos
        }
    }
    
    

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
