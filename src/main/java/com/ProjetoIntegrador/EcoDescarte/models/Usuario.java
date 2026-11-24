package com.ProjetoIntegrador.EcoDescarte.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data 
@Entity 
@Table (name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column (name = "nome", nullable = false, length = 100)
    private String nome;

    @Column (name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column (name = "senha_hash", nullable = false, length = 255)
    private String senha;

    @Column (name = "cidade", length = 100)
    private String cidade;

    @Column (name = "role", nullable = false)
    private String role = "ROLE_USER";

    @Column (name = "data_cadastro")
    private LocalDateTime dataCadastro = LocalDateTime.now();
}
