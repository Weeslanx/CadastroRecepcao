package com.servicos.cadastro_servicos.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String codDepartamento;
    private String codCorreios;

    
    public Setor() {}


    public Setor(Long id, String nome, String codDepartamento, String codCorreios) {
        this.id = id;
        this.nome = nome;
        this.codDepartamento = codDepartamento;
        this.codCorreios = codCorreios;

    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(String codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    // Método toString
    @Override
    public String toString() {
        return "Setor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codDepartamento='" + codDepartamento + '\'' +
                '}';
    }

    public String getCodCorreios() {
        return codCorreios;
    }

    public void setCodCorreios(String codCorreios) {
        this.codCorreios = codCorreios;
    }

    
}
