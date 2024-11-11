package com.servicos.cadastro_servicos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public final class Visita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "visitante_id", nullable = false)
    private Visitante visitante;

    @ManyToOne // Adiciona um relacionamento com Categoria
    @JoinColumn(name = "categoria_id", nullable = false) // Chave estrangeira para Categoria
    private Categoria categoria; // Relação com Categoria

    private String responsavel;
    private LocalDateTime horarioEntrada;
    private LocalDateTime horarioSaida;

    
    private int cracha; 
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    // Construtor padrão
    public Visita() {
    }

    // Construtor com parâmetros
    public Visita(Visitante visitante, Categoria categoria, String responsavel,
                  LocalDateTime horarioEntrada, LocalDateTime horarioSaida, int cracha) {
        this.visitante = visitante;
        this.categoria = categoria; // Usando Categoria diretamente
        this.responsavel = responsavel;
        this.horarioEntrada = horarioEntrada;
        this.horarioSaida = horarioSaida;
        setCracha(cracha);
    }
    
    public String getHorarioEntradaFormatado() {
        return horarioEntrada != null ? horarioEntrada.format(FORMATTER) : "Não disponível";
    }

    // Método para obter o horário de saída formatado
    public String getHorarioSaidaFormatado() {
        return horarioSaida != null ? horarioSaida.format(FORMATTER) : "Não disponível";
    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public Visitante getVisitante() {
        return visitante;
    }

    public void setVisitante(Visitante visitante) {
        this.visitante = visitante;
    }

    public Categoria getCategoria() {
        return categoria; // Mudando para retornar Categoria
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria; // Mudando para aceitar Categoria
    }

    // Outros getters e setters...


    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDateTime getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(LocalDateTime horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public LocalDateTime getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(LocalDateTime horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public int getCracha() {
        return cracha;
    }

    public void setCracha(int cracha) {
        if (cracha < 1 || cracha > 10) {
            throw new IllegalArgumentException("O crachá deve ser um número entre 1 e 10.");
        }
        this.cracha = cracha;
    }

    @Override
    public String toString() {
        return "Visita{" +
                "id=" + id +
                ", visitante=" + visitante +
                ", categoria='" + categoria + '\'' +
                ", responsavel='" + responsavel + '\'' +
                ", horarioEntrada=" + horarioEntrada +
                ", horarioSaida=" + horarioSaida +
                ", cracha=" + cracha +
                '}';
    }
}