package com.ProjetoIntegrador.EcoDescarte.Repositorios;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.ProjetoIntegrador.EcoDescarte.models.PontoDeColeta;

@Repository
public class PontoColetaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PontoDeColeta> listar(){
        String sql = """
                SELECT id, nome_local, endereco, bairro, cidade, horario_atendimento, ativo
                FROM pontos_coleta
                WHERE ativo = TRUE
                ORDER BY nome_local ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->{
            PontoDeColeta pc = new PontoDeColeta();
            pc.setId(rs.getLong("id"));
            pc.setNomeLocal(rs.getString("nome_local"));
            pc.setEndereco(rs.getString("endereco"));
            pc.setBairro(rs.getString("bairro"));
            pc.setCidade(rs.getString("cidade"));
            pc.setHorarioAtendimento(rs.getString("horario_atendimento"));
            pc.setAtivo(rs.getBoolean("ativo"));
            return pc;
        }); 
    }

    public List<PontoDeColeta> buscarPorEquip(Long equipId){
        String sql = """
                SELECT p.id, p.nome_local, p.endereco, p.bairro, p.cidade, p.horario_atendimento, p.ativo
                FROM pontos_coleta p
                INNER JOIN ponto_coleta_equipamento pce ON p.id = pce.ponto_coleta_id
                WHERE p.ativo = TRUE AND pce.equipamento_id = ?
                ORDER BY p.nome_local ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->{
            PontoDeColeta pc = new PontoDeColeta();
            pc.setId(rs.getLong("id"));
            pc.setNomeLocal(rs.getString("nome_local"));
            pc.setEndereco(rs.getString("endereco"));
            pc.setBairro(rs.getString("bairro"));
            pc.setCidade(rs.getString("cidade"));
            pc.setHorarioAtendimento(rs.getString("horario_atendimento"));
            pc.setAtivo(rs.getBoolean("ativo"));
            return pc;
        }, equipId); 
    }
}
