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
                                  "preco_sugerido REAL NOT NULL" +
                                  ");";

        String sqlAtendimentos = "CREATE TABLE IF NOT EXISTS atendimentos (" +
                                 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                 "dentista_id INTEGER NOT NULL," +
                                 "paciente_id INTEGER NOT NULL," +
                                 "procedimento_id INTEGER NOT NULL," +
                                 "horario TEXT NOT NULL," +
                                 "FOREIGN KEY (dentista_id) REFERENCES dentistas(id)," +
                                 "FOREIGN KEY (paciente_id) REFERENCES pacientes(id)," +
                                 "FOREIGN KEY (procedimento_id) REFERENCES procedimentos(id)" +
                                 ");";


        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sqlDentistas);
            stmt.execute(sqlPacientes);
            stmt.execute(sqlHistorico);
            stmt.execute(sqlHistoricoImagens);
            stmt.execute(sqlMateriais);
            stmt.execute(sqlProcedimentos);
            stmt.execute(sqlAtendimentos);

            System.out.println("Tabelas criadas ou já existentes.");

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}