package com.ProjetoIntegrador.EcoDescarte.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

@Data 
@Entity 
@Table(name = "equipamentos")
public class Equipamento {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Column(name = "icone", length = 255)
    private String icone;

    @Column(name = "req_limp_dados")
    private boolean reqLimpDados = false;

    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckList> checklists;
}