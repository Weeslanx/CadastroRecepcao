package com.servicos.cadastro_servicos.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class Estacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    private String motorista;

    private LocalDateTime hrEntrada;
    private LocalDateTime hrSaida;

    @ManyToOne
    @JoinColumn(name = "auditoria_id", nullable = true) // Permite valores nulos
    private Auditoria auditoria;


    // Construtor sem argumentos
    public Estacionamento() {
    }

    // Construtor com argumentos (exceto o ID)
    public Estacionamento(Veiculo veiculo, String motorista, LocalDateTime hrEntrada, LocalDateTime hrSaida, Auditoria auditoria) {
        this.veiculo = veiculo;
        this.motorista = motorista;
        this.hrEntrada = hrEntrada;
        this.hrSaida = hrSaida;
        this.auditoria = auditoria;
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public LocalDateTime getHrEntrada() {
        return hrEntrada;
    }

    public void setHrEntrada(LocalDateTime hrEntrada) {
        this.hrEntrada = hrEntrada;
    }

    public LocalDateTime getHrSaida() {
        return hrSaida;
    }

    public void setHrSaida(LocalDateTime hrSaida) {
        this.hrSaida = hrSaida;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    @Override
    public String toString() {
        return "Estacionamento{" +
                "id=" + id +
                ", veiculo=" + veiculo +
                ", motorista='" + motorista + '\'' +
                ", hrEntrada=" + hrEntrada +
                ", hrSaida=" + hrSaida +
                ", auditoria=" + auditoria +
                '}';
    }
}
