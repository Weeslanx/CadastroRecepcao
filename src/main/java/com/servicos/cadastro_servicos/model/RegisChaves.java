package com.servicos.cadastro_servicos.model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class RegisChaves{


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String requisi;

     @ManyToOne
    @JoinColumn(name = "chave_id")
    private Chave chave;
    private LocalDateTime saida;
    private LocalDateTime devol;


    public RegisChaves() {
       
    }
    

    @Override
    public String toString() {
        return "RegisChaves [id=" + id + ", requisi=" + requisi + ", chave=" + chave + ", saida=" + saida + ", devol="
                + devol + "]";
    }
    public RegisChaves(Long id, String requisi, Chave chave, LocalDateTime saida, LocalDateTime devol) {
        this.id = id;
        this.requisi = requisi;
        this.chave = chave;
        this.saida = saida;
        this.devol = devol;
    }
    public LocalDateTime getSaida() {
        return saida;
    }
    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }
    public LocalDateTime getDevol() {
        return devol;
    }
    public void setDevol(LocalDateTime devol) {
        this.devol = devol;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRequisi() {
        return requisi;
    }
    public void setRequisi(String requisi) {
        this.requisi = requisi;
    }
    public Chave getChave() {
        return chave;
    }
    public void setChave(Chave chave) {
        this.chave = chave;
    }



    






}