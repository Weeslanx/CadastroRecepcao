package com.servicos.cadastro_servicos.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class MatrixEffect implements Runnable {

    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String[] LOG_MESSAGES = {
        
        "Sistema operacional atualizado para a versão mais recente."
    };

    private static final String[] LOG_LEVELS = { "INFO", "WARN", "ERROR" };

    @PostConstruct
    public void startLogMatrix() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @Override 
    public void run() {
        try {
            while (true) {
                String timestamp = LocalDateTime.now().format(FORMATTER);
                String level = LOG_LEVELS[RANDOM.nextInt(LOG_LEVELS.length)];
                String message = LOG_MESSAGES[RANDOM.nextInt(LOG_MESSAGES.length)];

                System.out.println("\033[32m" + timestamp + " [" + level + "] " + message + "\033[0m");

                Thread.sleep(2000); // Exibe logs a cada 1 segundo
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}