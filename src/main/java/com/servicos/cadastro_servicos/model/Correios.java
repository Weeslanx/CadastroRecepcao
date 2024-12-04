package com.servicos.cadastro_servicos.model;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class Correios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String funcionario;
    private String setor;
    private LocalDateTime horario;
    private String codigoPostal; 
    private String destinatario;
    private String servico;
    private String entregador;


    public Correios() {}

    
    public Correios(Long id, String funcionario, String setor, LocalDateTime horario,
                           String servico, String codigoPostal, String destinatario) {
        this.id = id;
        this.funcionario = funcionario;
        this.setor = setor;
        this.horario = horario;
        this.servico = servico;
        this.codigoPostal = codigoPostal;
        this.destinatario = destinatario;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFuncionario() {
        return funcionario;
    }
    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }
    public String getSetor() {
        return setor;
    }
    public void setSetor(String setor) {
        this.setor = setor;
    }
    public LocalDateTime getHorario() {
        return horario;
    }
    public void setHorario(LocalDateTime horario) {
        this.horario = horario;
    }
    public String getCodigoPostal() {
        return codigoPostal;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    public String getDestinatario() {
        return destinatario;
    }
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }


    

    @Override
    public String toString() {
        return "Correios{" +
                "id=" + id +
                ", funcionario='" + funcionario + '\'' +
                ", setor='" + setor + '\'' +
                ", horario=" + horario +
                ", servico='" + servico + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", destinatario='" + destinatario + '\'' +
                '}';
    }


    public String getEntregador() {
        return entregador;
    }


    public void setEntregador(String entregador) {
        this.entregador = entregador;
    }

    

    
}
