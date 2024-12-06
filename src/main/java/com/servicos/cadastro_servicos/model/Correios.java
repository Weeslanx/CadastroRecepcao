package com.servicos.cadastro_servicos.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Correios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String funcionario;
    private LocalDateTime horario;
    private String codigoPostal;
    private String destinatario;
    private String servico;
    private String entregador;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "setor_id")
    private Setor setor;

    public Correios() {}

    public Correios(Long id, String funcionario, Setor setor, LocalDateTime horario,
                    String servico, String codigoPostal, String destinatario, String entregador) {
        this.id = id;
        this.funcionario = funcionario;
        this.setor = setor;
        this.horario = horario;
        this.servico = servico;
        this.codigoPostal = codigoPostal;
        this.destinatario = destinatario;
        this.entregador = entregador;
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

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
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

    public String getEntregador() {
        return entregador;
    }

    public void setEntregador(String entregador) {
        this.entregador = entregador;
    }

    @Override
    public String toString() {
        return "Correios{" +
                "id=" + id +
                ", funcionario='" + funcionario + '\'' +
                ", setor=" + (setor != null ? setor.getNome() : "null") +
                ", horario=" + horario +
                ", servico='" + servico + '\'' +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", destinatario='" + destinatario + '\'' +
                ", entregador='" + entregador + '\'' +
                '}';
    }
}
