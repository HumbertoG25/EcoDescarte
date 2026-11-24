package com.ProjetoIntegrador.EcoDescarte.Config;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final JdbcTemplate jdbcTemplate;

    public DataSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            popularEquipamentos();
            popularChecklists();
            popularPontosDeColeta();
            popularUsuarios();
            log.info("DataSeeder: verificação de dados iniciais concluída.");
        } catch (Exception e) {
            log.error("DataSeeder: erro ao popular dados iniciais: {}", e.getMessage(), e);
        }
    }

    private void popularEquipamentos() {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM equipamentos", Integer.class);
        if (total != null && total > 0) {
            return;
        }

        String sql = """
                INSERT INTO equipamentos (id, nome, categoria, icone, req_limp_dados)
                VALUES (?, ?, ?, ?, ?)
                ON CONFLICT (id) DO NOTHING
                """;

        jdbcTemplate.update(sql, 1L, "Smartphone / Celular", "Telefonia e Portáteis", "celular.png", true);
        jdbcTemplate.update(sql, 2L, "Notebook / Laptop", "Informática", "notebook.png", true);
        jdbcTemplate.update(sql, 3L, "Pilhas e Baterias", "Energia e Acumuladores", "bateria.png", false);
        jdbcTemplate.update(sql, 4L, "Monitor / Televisão", "Áudio e Vídeo", "monitor.png", false);
        jdbcTemplate.update(sql, 5L, "Cabos, Carregadores e Fones", "Acessórios e Periféricos", "cabos.png", false);

        sincronizarSequencia("equipamentos", "id");
    }

    private void popularChecklists() {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM checklists", Integer.class);
        if (total != null && total > 0) {
            return;
        }

        String sql = "INSERT INTO checklists (equipamento_id, instrucao, ordem) VALUES (?, ?, ?)";

        // 1 - Smartphone / Celular
        jdbcTemplate.update(sql, 1L, "Faça backup de fotos, contatos, conversas e arquivos importantes na nuvem ou computador.", 1);
        jdbcTemplate.update(sql, 1L, "Remova o chip da operadora (SIM card) e o cartão de memória (MicroSD).", 2);
        jdbcTemplate.update(sql, 1L, "Desvincule sua conta Google ou Apple ID (remova bloqueios de ativação).", 3);
        jdbcTemplate.update(sql, 1L, "Restaure o aparelho para as configurações originais de fábrica.", 4);
        jdbcTemplate.update(sql, 1L, "Se a bateria estiver inchada, coloque o aparelho em um saco plástico resistente e não tente dobrá-lo.", 5);

        // 2 - Notebook / Laptop
        jdbcTemplate.update(sql, 2L, "Salve seus documentos pessoais em um HD externo ou armazenamento em nuvem.", 1);
        jdbcTemplate.update(sql, 2L, "Saia de todas as contas logadas nos navegadores e aplicativos.", 2);
        jdbcTemplate.update(sql, 2L, "Formate o disco rígido (HD/SSD) ou execute a restauração completa do sistema operacional.", 3);
        jdbcTemplate.update(sql, 2L, "Guarde a fonte de alimentação (carregador) junto com o notebook para facilitar a triagem.", 4);

        // 3 - Pilhas e Baterias
        jdbcTemplate.update(sql, 3L, "Isole os polos positivo (+) e negativo (-) com fita isolante ou fita adesiva para evitar curto-circuito.", 1);
        jdbcTemplate.update(sql, 3L, "Armazene as pilhas e baterias em um recipiente plástico seco, longe de objetos metálicos (como chaves ou moedas).", 2);
        jdbcTemplate.update(sql, 3L, "Nunca amasse, perfure, queime ou tente abrir o invólucro da bateria.", 3);

        // 4 - Monitor / Televisão
        jdbcTemplate.update(sql, 4L, "Desconecte todos os cabos de energia, HDMI e antenas antes de movimentar o aparelho.", 1);
        jdbcTemplate.update(sql, 4L, "Enrole os cabos separados e prenda-os com elástico ou abraçadeira.", 2);
        jdbcTemplate.update(sql, 4L, "Evite apoiar objetos pesados sobre a tela para não quebrar o painel de vidro ou cristal líquido.", 3);

        // 5 - Cabos, Carregadores e Fones
        jdbcTemplate.update(sql, 5L, "Verifique se não há fios desencapados expostos; se houver, passe fita isolante nas pontas.", 1);
        jdbcTemplate.update(sql, 5L, "Enrole cada cabo individualmente para evitar emaranhados durante o transporte.", 2);
        jdbcTemplate.update(sql, 5L, "Separe carregadores com fonte das extensões ou fones simples, se possível.", 3);
    }

    private void popularPontosDeColeta() {
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM pontos_coleta", Integer.class);
        if (total != null && total > 0) {
            return;
        }

        String sql = """
                INSERT INTO pontos_coleta (id, nome_local, endereco, bairro, cidade, horario_atendimento, ativo)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (id) DO NOTHING
                """;

        jdbcTemplate.update(sql, 1L, "Ecoponto do Gama", "EQ 12/14, Área Especial", "Setor Central", "Gama - DF", "Seg a Sáb: 08h às 18h", true);
        jdbcTemplate.update(sql, 2L, "Administração Regional do Gama", "Setor Central, AE 01", "Setor Central", "Gama - DF", "Seg a Sex: 08h às 18h", true);
        jdbcTemplate.update(sql, 3L, "EcoPonto Setor Leste", "EQ 04/06, Área Especial", "Setor Leste", "Gama - DF", "Seg a Sex: 08h às 17h", true);
        jdbcTemplate.update(sql, 4L, "Ponto de Coleta Teste Inativo", "Setor Sul, Quadra 02", "Setor Sul", "Gama - DF", "Fechado", false);

        sincronizarSequencia("pontos_coleta", "id");

        long[][] vinculos = {
            {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5},
            {2, 1}, {2, 2}, {2, 3},
            {3, 1}, {3, 3}, {3, 5}
        };

        String sqlVinculo = """
                INSERT INTO ponto_coleta_equipamento (ponto_coleta_id, equipamento_id)
                SELECT ?, ?
                WHERE NOT EXISTS (
                    SELECT 1 FROM ponto_coleta_equipamento
                    WHERE ponto_coleta_id = ? AND equipamento_id = ?
                )
                """;

        for (long[] v : vinculos) {
            jdbcTemplate.update(sqlVinculo, v[0], v[1], v[0], v[1]);
        }
    }

    private void popularUsuarios() {
        inserirUsuarioSeNaoExistir("Administrador EcoDescarte", "admin@ecodescarte.com", "admin123", "Gama - DF", "ROLE_ADMIN");
        inserirUsuarioSeNaoExistir("Maria Silva", "maria@email.com", "123456", "Gama - DF", "ROLE_USER");
        inserirUsuarioSeNaoExistir("João Souza", "joao@email.com", "123456", "Brasília - DF", "ROLE_USER");
    }

    private void inserirUsuarioSeNaoExistir(String nome, String email, String senha, String cidade, String role) {
        Integer existe = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM usuarios WHERE LOWER(email) = LOWER(?)", Integer.class, email);
        if (existe != null && existe > 0) {
            return;
        }

        jdbcTemplate.update(
                "INSERT INTO usuarios (nome, email, senha_hash, cidade, role, data_cadastro) VALUES (?, ?, ?, ?, ?, ?)",
                nome, email, senha, cidade, role, Timestamp.valueOf(LocalDateTime.now()));
    }

    private void sincronizarSequencia(String tabela, String coluna) {
        String seq = jdbcTemplate.queryForObject(
                "SELECT pg_get_serial_sequence(?, ?)", String.class, tabela, coluna);
        if (seq != null && !seq.isBlank()) {
            jdbcTemplate.queryForObject(
                    "SELECT setval(?, COALESCE((SELECT MAX(" + coluna + ") FROM " + tabela + "), 1), true)",
                    Long.class, seq);
        }
    }
}
