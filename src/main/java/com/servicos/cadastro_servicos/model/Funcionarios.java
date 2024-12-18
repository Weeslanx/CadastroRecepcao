package com.servicos.cadastro_servicos.model;



import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Funcionarios {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setor_id")
    private Setor setor;



    

    @Override
    public String toString() {
        return "Funcionarios [id=" + id + ", name=" + name + ", setor=" + setor + "]";
    }

    public Funcionarios() {
        
    }

    public Funcionarios(Long id, String name, Setor setor) {
        this.id = id;
        this.name = name;
        this.setor = setor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    



    


    


    



    


    
    
}
