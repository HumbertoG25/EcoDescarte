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

    public List<PontoDeColeta> listartodos(){
        String sql = """
                SELECT id, nome_local, endereco, bairro, cidade, horario_atendimento, ativo
                FROM pontos_coleta
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

    public Long inserir(PontoDeColeta ponto) {
        String sql = """
                INSERT INTO pontos_coleta (nome_local, endereco, bairro, cidade, horario_atendimento, ativo)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id
                """;
        return jdbcTemplate.queryForObject(sql, Long.class,
            ponto.getNomeLocal(),
            ponto.getEndereco(),
            ponto.getBairro(),
            ponto.getCidade(),
            ponto.getHorarioAtendimento(),
            ponto.isAtivo()
        );
    }

    public void vincularEquipamento(Long pontoColetaId, Long equipamentoId) {
        String sql = """
                INSERT INTO ponto_coleta_equipamento (ponto_coleta_id, equipamento_id)
                VALUES (?, ?)
                ON CONFLICT (ponto_coleta_id, equipamento_id) DO NOTHING
                """;
        jdbcTemplate.update(sql, pontoColetaId, equipamentoId);
    }

    public void desvincularEquipamento(Long pontoColetaId, Long equipamentoId) {
        String sql = "DELETE FROM ponto_coleta_equipamento WHERE ponto_coleta_id = ? AND equipamento_id = ?";
        jdbcTemplate.update(sql, pontoColetaId, equipamentoId);
    }

    public void deletar(Long id) {
        String sql = "DELETE FROM pontos_coleta WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
