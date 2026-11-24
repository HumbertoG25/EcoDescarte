package com.ProjetoIntegrador.EcoDescarte.Repositorios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.ProjetoIntegrador.EcoDescarte.models.Equipamento;

@Repository
public class EquipamentoRepository {
    
    @Autowired 
    private JdbcTemplate jdbcTemplate;

    public List<Equipamento> listarTodos() {
        String sql = "SELECT id, nome, categoria, icone, req_limp_dados FROM equipamentos ORDER BY nome ASC";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Equipamento eq = new Equipamento();
            eq.setId(rs.getLong("id"));
            eq.setNome(rs.getString("nome"));
            eq.setCategoria(rs.getString("categoria"));
            eq.setIcone(rs.getString("icone"));
            eq.setReqLimpDados(rs.getBoolean("req_limp_dados"));
            return eq;
        });
    }
}
