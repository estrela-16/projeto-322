package DAO;
import Principal.Dentista;
import Principal.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Importe sua classe Dentista aqui (assumindo que você a tenha)
// Exemplo: import seu_pacote.Dentista;

public class DentistaDAO {

    public void inserir(Dentista dentista) {
        // Adiciona RETURN_GENERATED_KEYS para informar ao PreparedStatement
        // que queremos recuperar as chaves geradas automaticamente (o ID)
        String sql = "INSERT INTO dentistas (nome, cpf, telefone, cro) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getCpf());
            stmt.setString(3, dentista.getTelefone());
            stmt.setString(3, dentista.getCro());
            stmt.executeUpdate(); // Executa a inserção

            // Agora, vamos recuperar o ID gerado pelo banco de dados
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) { // Se houver um ID gerado (deve haver para AUTOINCREMENT)
                    int idGerado = rs.getInt(1); // Pega o primeiro (e geralmente único) ID gerado
                    dentista.setId(idGerado); // Define o ID gerado no objeto Dentista
                    System.out.println("Dentista inserido com sucesso! ID gerado: " + idGerado);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inserir dentista: " + e.getMessage());
        }
    }


    //Faz uma lista com todos os dentistas armazenados na base de dados
    public List<Dentista> buscarTodos() {
        List<Dentista> dentistas = new ArrayList<>();
        String sql = "SELECT * FROM dentistas";
        try (Connection conn = ConexaoBD.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Dentista dentista = new Dentista(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone"),
                    rs.getString("cro")
                );
                dentistas.add(dentista);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar dentistas: " + e.getMessage());
        }
        return dentistas;
    }

    // Métodos para atualizar e deletar podem ser adicionados aqui
    public void atualizar(Dentista dentista) {
        String sql = "UPDATE dentistas SET nome = ?, cpf = ?, telefone = ?, cro = ? WHERE id = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dentista.getNome());
            stmt.setString(2, dentista.getCpf());
            stmt.setString(3, dentista.getTelefone());
            stmt.setString(5, dentista.getCro());
            stmt.executeUpdate();
            System.out.println("Dentista atualizado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar dentista: " + e.getMessage());
        }
    }

    //Deleta dentista da base de dados
    public void deletar(int id) {
        String sql = "DELETE FROM dentistas WHERE id = ?";
        try (Connection conn = ConexaoBD.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Dentista deletado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar dentista: " + e.getMessage());
        }
    }
}