package Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc:sqlite:consultorio_odontologico.db"; // Nome do seu arquivo de banco de dados

    public static Connection conectar() {
        Connection conn = null;
        try {
            // Carrega o driver JDBC do SQLite
            Class.forName("org.sqlite.JDBC");
            // Cria a conexão com o banco de dados
            conn = DriverManager.getConnection(URL);
            System.out.println("Conexão com o banco de dados estabelecida.");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do SQLite não encontrado: " + e.getMessage());
        }
        return conn;
    }

    public static void fecharConexao(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Conexão com o banco de dados fechada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão com o banco de dados: " + e.getMessage());
        }
    }
}