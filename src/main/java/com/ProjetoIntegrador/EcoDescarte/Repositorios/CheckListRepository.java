package com.ProjetoIntegrador.EcoDescarte.Repositorios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ProjetoIntegrador.EcoDescarte.models.CheckList;

@Repository
public class CheckListRepository {
    @Autowired 
    private JdbcTemplate jdbcTemplate;

    public List<CheckList> buscarPorEquipID(Long EquipID){
        String sql = """
                     SELECT id, equipamento_id, instrucao, ordem
                     FROM checklists
                     WHERE equipamento_id = ?
                     ORDER BY ordem ASC
                     """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CheckList cl = new CheckList();
            cl.setId(rs.getLong("id"));
            cl.setInstrucao(rs.getString("instrucao"));
            cl.setOrdem(rs.getInt("ordem"));
            return cl;
        }, EquipID);
    }

    public List<CheckList> Listar(){
        String sql = """
                     SELECT id, equipamento_id, instrucao, ordem
                     FROM checklists
                     ORDER BY equipamento_id ASC, ordem ASC
                     """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CheckList item = new CheckList();
            item.setId(rs.getLong("id"));
            item.setInstrucao(rs.getString("instrucao"));
            item.setOrdem(rs.getInt("ordem"));
            return item;
        });
    }
}
