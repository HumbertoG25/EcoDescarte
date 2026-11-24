package com.ProjetoIntegrador.EcoDescarte.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

@Data 
@Entity 
@Table(name = "pontos_coleta")
public class PontoDeColeta {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_local", nullable = false, length = 150)
    private String nomeLocal;

    @Column(name = "endereco", nullable = false, length = 255)
    private String endereco;

    @Column(name = "bairro", length = 100)
    private String bairro;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Column(name = "horario_atendimento", length = 150)
    private String horarioAtendimento;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    // Relacionamento N:N com a tabela associativa ponto_coleta_equipamento
    @ManyToMany
    @JoinTable(
        name = "ponto_coleta_equipamento",
        joinColumns = @JoinColumn(name = "ponto_coleta_id"),
        inverseJoinColumns = @JoinColumn(name = "equipamento_id")
    )
    private List<Equipamento> materiaisAceitos;
}
