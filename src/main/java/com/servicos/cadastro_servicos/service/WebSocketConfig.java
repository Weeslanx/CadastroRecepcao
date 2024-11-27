package com.servicos.cadastro_servicos.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Define o prefixo para os tópicos de mensagem
        config.setApplicationDestinationPrefixes("/app"); // Prefixo para destinos do cliente
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Endpoint para conexão WebSocket
                .setAllowedOriginPatterns("*") // Permite conexões de qualquer origem
                .withSockJS(); // Habilita fallback com SockJS
    }
}

