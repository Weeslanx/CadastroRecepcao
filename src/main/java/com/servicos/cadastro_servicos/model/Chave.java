package com.servicos.cadastro_servicos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Chave {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cod;
    private String local;

    

    
    @Override
    public String toString() {
        return "Chave [id=" + id + ", cod=" + cod + ", local=" + local + "]";
    }

    public Chave() {
        
        }
        
    public Chave(Long id, Integer cod, String local) {
        this.id = id;
        this.cod = cod;
        this.local = local;
    }

    


    


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getCod() {
        return cod;
    }
    public void setCod(Integer cod) {
        this.cod = cod;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    

    

    
}
