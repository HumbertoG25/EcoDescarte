package com.ProjetoIntegrador.EcoDescarte.Repositorios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ProjetoIntegrador.EcoDescarte.models.Usuario;

@Repository 
public class UsuarioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate; 

    public Optional<Usuario> buscarEmail(String email){
        String sql = """
                    SELECT id, nome, email, senha_hash, cidade, role, data_cadastro
                    FROM usuarios
                    WHERE LOWER(email) = LOWER(?)
                    """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha_hash"));
        usuario.setCidade(rs.getString("cidade"));
        usuario.setRole(rs.getString("role"));

        var dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            usuario.setDataCadastro(dataCadastro.toLocalDateTime());
        }

        return usuario;
        }, email).stream().findFirst();
    }

    public void Insert(Usuario usuario){
        String sql = "INSERT INTO usuarios (nome, email, senha_hash, cidade, role, data_cadastro) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getSenha(),
            usuario.getCidade(),
            usuario.getRole(),
            java.sql.Timestamp.valueOf(usuario.getDataCadastro() != null ? usuario.getDataCadastro() : java.time.LocalDateTime.now())
        );
    }
}
