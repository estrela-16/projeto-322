package Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CriadorTabelas {

    public static void criarTabelas() {
        String sqlDentistas = "CREATE TABLE IF NOT EXISTS dentistas (" +
                              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                              "nome TEXT NOT NULL," +
                              "cpf TEXT UNIQUE NOT NULL," +
                              "telefone TEXT," +
                              "cro TEXT UNIQUE NOT NULL" +
                              ");";

        String sqlPacientes = "CREATE TABLE IF NOT EXISTS pacientes (" +
                              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                              "nome TEXT NOT NULL," +
                              "cpf TEXT UNIQUE NOT NULL," +
                              "telefone TEXT" +
                              ");";

        String sqlHistorico = "CREATE TABLE IF NOT EXISTS historicos (" +
                              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                              "paciente_id INTEGER," +
                              "descricao TEXT," +
                              "FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE" +
                              ");";

        String sqlHistoricoImagens = "CREATE TABLE IF NOT EXISTS historico_imagens (" +
                                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                     "historico_id INTEGER NOT NULL," +
                                     "caminho_imagem TEXT NOT NULL," +
                                     "FOREIGN KEY (historico_id) REFERENCES historicos(id) ON DELETE CASCADE" +
                                     ");";

        String sqlMateriais = "CREATE TABLE IF NOT EXISTS materiais (" +
                              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                              "nome TEXT NOT NULL UNIQUE," +
                              "valor REAL NOT NULL" +
                              ");";

         String sqlProcedimentos = "CREATE TABLE IF NOT EXISTS procedimentos (" +
                                  "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                  "nome TEXT NOT NULL UNIQUE," +
                                  "especialidade TEXT NOT NULL" + 
                                  ");";

        // ligar procedimentos e materiais
        String sqlProcedimentoMateriais = "CREATE TABLE IF NOT EXISTS procedimento_materiais (" +
                                          "procedimento_id INTEGER NOT NULL," +
                                          "material_id INTEGER NOT NULL," +
                                          "FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id) ON DELETE CASCADE," +
                                          "FOREIGN KEY (material_id) REFERENCES materiais(id) ON DELETE CASCADE," +
                                          "PRIMARY KEY (procedimento_id, material_id)" +
                                          ");";

        String sqlAtendimentos = "CREATE TABLE IF NOT EXISTS atendimentos (" +
                                 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                 "data TEXT NOT NULL," +
                                 "horario TEXT NOT NULL," +
                                 "status_pagamento TEXT NOT NULL," +
                                 "dentista_id INTEGER NOT NULL," +
                                 "paciente_id INTEGER NOT NULL," +
                                 "procedimento_id INTEGER NOT NULL," +
                                 "FOREIGN KEY (dentista_id) REFERENCES dentistas(id)," +
                                 "FOREIGN KEY (paciente_id) REFERENCES pacientes(id)," +
                                 "FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id)" +
                                 ");";
        String sqlContas = "CREATE TABLE IF NOT EXISTS contas (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "nome TEXT NOT NULL UNIQUE," +
                            "valor REAL NOT NULL" +
                            ");";

        String sqlMateriaisComuns = "CREATE TABLE IF NOT EXISTS materiais_comuns (" +
                                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    "nome TEXT NOT NULL UNIQUE," +
                                    "valor REAL NOT NULL," +
                                    "quantidade INTEGER NOT NULL" +
                                    ");";
        
    
        String sqlGastosConfig = "CREATE TABLE IF NOT EXISTS gastos_config (" +
                                 "id INTEGER PRIMARY KEY," +
                                 "consultas_mes INTEGER," +
                                 "comissao REAL," +
                                 "taxa_servico REAL" +
                                 ");";


        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlDentistas);
            stmt.execute(sqlPacientes);
            stmt.execute(sqlHistorico);
            stmt.execute(sqlHistoricoImagens);
            stmt.execute(sqlMateriais);
            stmt.execute(sqlProcedimentos);
            stmt.execute(sqlProcedimentoMateriais);
            stmt.execute(sqlAtendimentos);
            stmt.execute(sqlContas);
            stmt.execute(sqlMateriaisComuns);
            stmt.execute(sqlGastosConfig);

            System.out.println("Tabelas criadas ou já existentes.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}