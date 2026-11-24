package com.ProjetoIntegrador.EcoDescarte.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data 
@Entity 
@Table (name = "checklists")
public class CheckList {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne 
    @JoinColumn (name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Column (name = "instrucao", nullable = false)
    private String instrucao;

    @Column (name = "ordem", nullable = false)
    private int ordem;

    @Transient
    private boolean concluido;
}
